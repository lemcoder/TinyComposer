package pl.lemanski.tc.domain.service.audio

import io.github.lemcoder.mikrosoundfont.io.toByteArrayLittleEndian
import io.github.lemcoder.mikrosoundfont.io.wav.WavFileHeader
import io.github.lemcoder.mikrosoundfont.io.wav.toByteArray
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.toNSData
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.duration
import platform.AVFoundation.isPlaybackLikelyToKeepUp
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSDataWritingAtomic
import platform.Foundation.NSFileManager
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.writeToFile
import platform.darwin.Float64
import platform.darwin.NSEC_PER_SEC
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun playAudio(data: FloatArray, sampleRate: Int) = suspendCoroutine { continuation ->
    val logger = Logger(AudioService::class)
    logger.debug("Playing audio on iOS")
    logger.debug("Sample rate: $sampleRate")
    logger.debug("Data size: ${data.size}")

    val playbackScope = CoroutineScope(SupervisorJob())

    playbackScope.launch(Dispatchers.Main.immediate) {
        val bytes = WavFileHeader.write(sampleRate.toUInt(), data.size.toUInt(), 2u).toByteArray()
        val audioData = (bytes + data.toByteArrayLittleEndian()).toNSData()
        val tempDirectoryPath = NSTemporaryDirectory()
        val fileManager = NSFileManager.defaultManager
        val tmpDirURL = NSURL.fileURLWithPath(tempDirectoryPath, isDirectory = true)
        val soundFileURL = tmpDirURL.URLByAppendingPathComponent("temp")?.URLByAppendingPathExtension("wav")

        fileManager.createDirectoryAtURL(tmpDirURL, withIntermediateDirectories = false, attributes = null, error = null)
        val filePath = soundFileURL?.path ?: throw IllegalStateException("File path is null")
        audioData.writeToFile(filePath, NSDataWritingAtomic, null)

        AudioPlayer(
            PlayerState(
                isPlaying = false,
                isBuffering = false,
                currentTime = 0L
            )
        ).apply {
            addSongUrl(filePath)
            play()
        }

        delay(1000)
        continuation.resume(Unit)
    }
}

data class PlayerState(
    var isPlaying: Boolean,
    var isBuffering: Boolean,
    var currentTime: Long,
    var duration: Long = 0L
)

internal class AudioPlayer(private val playerState: PlayerState) {
    private var playerItem: AVPlayerItem? = null
    private var avAudioPlayer: AVPlayer = AVPlayer()
    private lateinit var timeObserver: Any

    @OptIn(ExperimentalForeignApi::class)
    private val observer: (CValue<CMTime>) -> Unit = { time: CValue<CMTime> ->
        playerState.isBuffering = avAudioPlayer.currentItem?.isPlaybackLikelyToKeepUp() != true
        playerState.isPlaying = avAudioPlayer.timeControlStatus == AVPlayerTimeControlStatusPlaying
        val rawTime: Float64 = CMTimeGetSeconds(time)
        val parsedTime = rawTime.toDuration(DurationUnit.SECONDS).inWholeSeconds
        playerState.currentTime = parsedTime
        if (avAudioPlayer.currentItem != null) {
            val cmTime = CMTimeGetSeconds(avAudioPlayer.currentItem!!.duration)
            playerState.duration = if (cmTime.isNaN()) 0 else cmTime.toDuration(DurationUnit.SECONDS).inWholeSeconds
        }
    }

    init {
        setUpAudioSession()
        playerState.isPlaying = avAudioPlayer.timeControlStatus == AVPlayerTimeControlStatusPlaying
    }

    fun play() {
        stop()
        startTimeObserver()
        playerState.isBuffering = true
        avAudioPlayer.replaceCurrentItemWithPlayerItem(playerItem)
        avAudioPlayer.play()
    }

    fun pause() {
        avAudioPlayer.pause()
        playerState.isPlaying = false
    }

    @OptIn(ExperimentalForeignApi::class)
    fun seekTo(time: Double) {
        playerState.isBuffering = true
        val cmTime = CMTimeMakeWithSeconds(time, NSEC_PER_SEC.toInt())
        avAudioPlayer.currentItem?.seekToTime(time = cmTime, completionHandler = {
            playerState.isBuffering = false
        })
    }

    fun addSongUrl(songUrl: String) {
        playerItem = AVPlayerItem(uRL = NSURL.fileURLWithPath(songUrl))
        avAudioPlayer = AVPlayer.playerWithPlayerItem(playerItem)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setUpAudioSession() {
        try {
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryPlayback, null)
            audioSession.setActive(true, null)
        } catch (e: Exception) {
            println("Error setting up audio session: ${e.message}")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun startTimeObserver() {
        val interval = CMTimeMakeWithSeconds(1.0, NSEC_PER_SEC.toInt())
        timeObserver = avAudioPlayer.addPeriodicTimeObserverForInterval(interval, null, observer)
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = avAudioPlayer.currentItem,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
                // TODO notify about end
            }
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun stop() {
        if (::timeObserver.isInitialized) avAudioPlayer.removeTimeObserver(timeObserver)
        avAudioPlayer.pause()
        avAudioPlayer.currentItem?.seekToTime(CMTimeMakeWithSeconds(0.0, NSEC_PER_SEC.toInt()))
    }
}
