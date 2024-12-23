package pl.lemanski.tc.domain.service.audio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.yield
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.exception.ApplicationStateException
import kotlin.coroutines.resume

internal suspend fun playAudio(data: FloatArray, sampleRate: Int) = suspendCancellableCoroutine { continuation ->
    val playbackScope = CoroutineScope(Job())
    val logger = Logger(AudioService::class)
    val convertedData = convertPCM32ToPCM16(data)

    val bufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    // Create an AudioTrack instance
    val audioTrack = AudioTrack(
        AudioManager.STREAM_MUSIC,
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize,
        AudioTrack.MODE_STREAM
    );

    // Start playing
    audioTrack.play();

    audioTrack.setNotificationMarkerPosition(convertedData.size)
    audioTrack.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
        override fun onMarkerReached(track: AudioTrack?) {
            logger.debug("Audio playback completed")
            continuation.resume(Unit)
            audioTrack.stop()
            audioTrack.release()
        }

        override fun onPeriodicNotification(track: AudioTrack?) {
            // TODO monitor playback position
        }
    })

    playbackScope.launch {
        var offset = 0
        while (offset < convertedData.size) {
            if (!this@launch.isActive) {
                break
            }

            val length = minOf(bufferSize, convertedData.size - offset)
            val bytesWritten = audioTrack.write(convertedData, offset, length)

            if (bytesWritten < 0) {
                throw ApplicationStateException("Error writing audio data")
            }

            offset += bytesWritten
        }
    }

    continuation.invokeOnCancellation {
        logger.debug("Audio playback cancelled")
        playbackScope.cancel()
        audioTrack.stop()
        audioTrack.release()
    }
}

private fun convertPCM32ToPCM16(f32Data: FloatArray): ShortArray = ShortArray(f32Data.size) { i ->
    (f32Data[i].coerceIn(-1.0f, 1.0f) * 32767).toInt().toShort()
}