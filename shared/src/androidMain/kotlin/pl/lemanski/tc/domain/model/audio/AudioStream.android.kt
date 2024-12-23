package pl.lemanski.tc.domain.model.audio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.lemanski.tc.domain.service.audio.AudioService
import pl.lemanski.tc.utils.Logger

private interface OnPlaybackMarkerReachedListener {
    fun onPositionReached(position: Int)
    fun onEndOfStreamReached()
}

private object PlaybackManager {
    private var state = State.STOPPED
    private var isLooping = false
    private var playbackJob: Job = Job()

    // set no-op listener to avoid NPE
    private var listener: OnPlaybackMarkerReachedListener = object : OnPlaybackMarkerReachedListener {
        override fun onPositionReached(position: Int) {
            // no-op
        }

        override fun onEndOfStreamReached() {
            // no-op
        }
    }

    private const val SAMPLE_RATE = AudioStream.SAMPLE_RATE

    private val playbackScope = CoroutineScope(SupervisorJob())
    private val logger = Logger(AudioService::class)

    private val minBufferSize: Int = AudioTrack.getMinBufferSize(
        /* sampleRateInHz = */ SAMPLE_RATE,
        /* channelConfig = */ AudioFormat.CHANNEL_OUT_MONO, // TODO enable stereo output
        /* audioFormat = */ AudioFormat.ENCODING_PCM_16BIT
    )

    private val audioTrack: AudioTrack = AudioTrack(
        /* streamType = */ AudioManager.STREAM_MUSIC,
        /* sampleRateInHz = */ SAMPLE_RATE,
        /* channelConfig = */ AudioFormat.CHANNEL_OUT_MONO, // TODO enable stereo output
        /* audioFormat = */ AudioFormat.ENCODING_PCM_16BIT,
        /* bufferSizeInBytes = */ minBufferSize,
        /* mode = */ AudioTrack.MODE_STREAM
    )

    init {
        logger.info("AudioManager initialized")
    }

    fun setNotificationListener(listener: OnPlaybackMarkerReachedListener) {
        logger.info("Setting notification listener")
        this.listener = listener
    }

    fun setLooping(looping: Boolean) {
        logger.info("Setting looping to $looping")
        isLooping = looping
    }

    fun play(data: FloatArray) {
        logger.info("Playing audio data")
        if (state == State.PLAYING) {
            logger.error("Audio is already playing")
            return
        }

        state = State.PLAYING

        logger.info("Converting audio data to PCM16")
        val convertedData = convertPCM32ToPCM16(data)

        audioTrack.play()

        audioTrack.setNotificationMarkerPosition(convertedData.size) // set marker at the end of the data

        playbackJob = playbackScope.launch {
            logger.info("Starting audio track")
            logger.info("Converted data size: ${convertedData.size}")
            var offset = 0
            while (offset < convertedData.size) {
                if (!this@launch.isActive) {
                    break
                }

                val length = minOf(minBufferSize, convertedData.size - offset)
                val bytesWritten = audioTrack.write(convertedData, offset, length)

                if (bytesWritten < 0) {
                    logger.error("Error writing audio data")
                    break
                }

                // post every 1% of data size to avoid flooding the listener
                listener.onPositionReached(offset)

                offset += bytesWritten

                if (isLooping) {
                    if (offset >= convertedData.size) {
                        offset = 0
                        listener.onEndOfStreamReached()
                    }
                }
            }
        }
    }

    fun stop() {
        if (state == State.STOPPED) {
            logger.error("Audio is already stopped")
            return
        }

        runBlocking {
            playbackJob.cancelAndJoin()
        }

        audioTrack.pause()
        audioTrack.flush()
        state = State.STOPPED
    }

    // TODO add pause and resume methods

    //---

    private fun convertPCM32ToPCM16(f32Data: FloatArray): ShortArray = ShortArray(f32Data.size) { i ->
        (f32Data[i].coerceIn(-1.0f, 1.0f) * 32767).toInt().toShort()
    }

    //---

    enum class State {
        PLAYING,
        STOPPED
    }
}

internal actual fun AudioStream.play(isLoopingEnabled: Boolean) {
    val logger = Logger(AudioStream::class)
    val notificationListener = object : OnPlaybackMarkerReachedListener {
        private var lastMarker = -1

        override fun onEndOfStreamReached() {
            logger.info("End of stream reached")
            if (isLoopingEnabled) {
                logger.info("Looping enabled, no-op on marker reached")
                return
            }

            logger.info("Looping disabled, invoking callback")
            markerReachedCallback?.invoke(AudioStream.END_MARKER) // indicate that the end of the stream has been reached
        }

        override fun onPositionReached(position: Int) {
            logger.info("Playback head position: $position")
            val nearestMarker = markers
                .filter { it.position <= position }
                .minByOrNull { position - it.position } ?: return // Find the nearest one above

            if (nearestMarker.index == lastMarker) {
                return
            }
            lastMarker = nearestMarker.index
            markerReachedCallback?.invoke(nearestMarker)
        }
    }

    PlaybackManager.setNotificationListener(notificationListener)

    PlaybackManager.setLooping(isLoopingEnabled)

    PlaybackManager.play(data)
}

internal actual fun AudioStream.stop() {
    PlaybackManager.stop()
}