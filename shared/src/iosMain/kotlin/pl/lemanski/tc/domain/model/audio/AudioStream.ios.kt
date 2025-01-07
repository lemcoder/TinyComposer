package pl.lemanski.tc.domain.model.audio

import io.github.lemcoder.mikrosoundfont.io.toByteArrayLittleEndian
import io.github.lemcoder.mikrosoundfont.io.wav.WavFileHeader
import io.github.lemcoder.mikrosoundfont.io.wav.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import pl.lemanski.tc.domain.service.audio.AudioService
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.asDispatchQueue
import pl.lemanski.tc.utils.nativeRunCatching
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerLooper
import platform.AVFoundation.AVQueuePlayer
import platform.AVFoundation.addBoundaryTimeObserverForTimes
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSURL
import platform.darwin.NSEC_PER_SEC

internal interface OnPlaybackMarkerReachedListener {
    fun onTimeReached(time: Double)
    fun onEndOfStreamReached()
}

@OptIn(ExperimentalForeignApi::class)
internal class AudioPlayer {
    // set no-op listener to avoid NPE
    private var listener: OnPlaybackMarkerReachedListener = object : OnPlaybackMarkerReachedListener {
        override fun onTimeReached(time: Double) {
            // no-op
        }

        override fun onEndOfStreamReached() {
            // no-op
        }
    }
    private var isLooping = false
    private var looper: AVPlayerLooper? = null
    private var player: AVQueuePlayer? = null

    private val playbackScope = CoroutineScope(SupervisorJob())
    private var playbackJob: Job = Job()

    internal fun setOnPlaybackMarkerReachedListener(listener: OnPlaybackMarkerReachedListener) {
        this.listener = listener
    }

    internal fun setLooping(looping: Boolean) {
        isLooping = looping
    }

    @OptIn(ExperimentalForeignApi::class)
    internal fun play(
        data: FloatArray,
        channelCount: Int,
    ) {
        val logger = Logger(AudioService::class)

        logger.debug("Writing data to file")
        val tmpFilePath = writeDataToTmpFile(data, AudioStream.SAMPLE_RATE, channelCount) ?: run {
            logger.error("Error writing data to file")
            return
        }

        logger.debug("Setting up audio session")
        val audioSession = AVAudioSession.sharedInstance()

        nativeRunCatching {
            audioSession.setCategory(AVAudioSessionCategoryPlayback, it)

            onError {
                logger.error("Error setting up audio session")
            }
        }


        nativeRunCatching {
            audioSession.setActive(true, it)

            onError {
                logger.error("Error setting up audio session")
            }
        }

        logger.debug("Creating player")
        val item = AVPlayerItem(uRL = NSURL.fileURLWithPath(tmpFilePath))

        playbackJob = playbackScope.launch {
            player = AVQueuePlayer.playerWithPlayerItem(item)
            looper = AVPlayerLooper.playerLooperWithPlayer(player = player!!, templateItem = item)

            if (!isLooping) {
                looper?.disableLooping()
            }

            player?.addPeriodicTimeObserverForInterval(
                interval = CMTimeMakeWithSeconds(0.001, NSEC_PER_SEC.toInt()),
                queue = Dispatchers.IO.asDispatchQueue()
            ) { time ->
                val currentTime = CMTimeGetSeconds(time)
                listener.onTimeReached(currentTime)
            }

            player?.addBoundaryTimeObserverForTimes(
                times = listOf(CMTimeGetSeconds(item.duration)),
                queue = Dispatchers.IO.asDispatchQueue()
            ) {
                logger.debug("Audio playback completed")
                listener.onEndOfStreamReached()

                if (!isLooping) {
                    deleteFile(tmpFilePath)
                }
            }

            logger.debug("Playing audio")
            player?.play()
        }
    }

    fun stop() {
        playbackJob.cancel()
        player?.pause()
        player = null
        looper = null
    }

    // ---


    private fun writeDataToTmpFile(data: FloatArray, sampleRate: Int, channelCount: Int): String? {
        val wavHeader = WavFileHeader.write(sampleRate.toUInt(), data.size.toUInt(), channelCount.toUShort()).toByteArray()
        val audioData = wavHeader + data.toByteArrayLittleEndian()

        val path = Path(SystemTemporaryDirectory, "audio.wav")
        val sink = SystemFileSystem.sink(path).buffered()

        sink.write(audioData)
        sink.close()

        return path.toString().takeIf { SystemFileSystem.exists(path) }
    }

    private fun deleteFile(filePath: String) {
        SystemFileSystem.delete(Path(filePath))
    }

    companion object {
        val Instance = AudioPlayer()
    }
}

internal actual fun AudioStream.play(isLoopingEnabled: Boolean, tempo: Int) {
    val logger = Logger(AudioService::class)

    try {
        logger.debug("Init playing audio")

        val player = AudioPlayer.Instance
        player.setLooping(isLoopingEnabled)

        player.setOnPlaybackMarkerReachedListener(object : OnPlaybackMarkerReachedListener {
            private var lastMarker = -1

            override fun onTimeReached(time: Double) {
                val markerPosition = time * AudioStream.SAMPLE_RATE
                val nearestMarker = markers
                    .filter { it.position <= markerPosition }
                    .minByOrNull { markerPosition - it.position } ?: return // Find the nearest one above

                if (nearestMarker.index == lastMarker) {
                    return
                }
                lastMarker = nearestMarker.index
                markerReachedCallback?.invoke(nearestMarker)
            }

            override fun onEndOfStreamReached() {
                logger.info("End of stream reached")
                if (isLoopingEnabled) {
                    logger.info("Looping enabled, no-op on marker reached")
                    return
                }

                logger.info("Looping disabled, invoking callback")
                markerReachedCallback?.invoke(AudioStream.END_MARKER) // indicate that the end of the stream has been reached
            }
        })

        logger.debug("Playing audio")
        player.play(
            data = this.data,
            channelCount = this.output.value,
        )
    } catch (e: Exception) {
        logger.error("Error playing audio", e)
    }
}

internal actual fun AudioStream.stop() {
    val logger = Logger(AudioService::class)

    AudioPlayer.Instance.stop()

    logger.debug("Stopping audio")
}