package pl.lemanski.tc.domain.service.audio

import io.github.lemcoder.mikrosoundfont.io.toByteArrayLittleEndian
import io.github.lemcoder.mikrosoundfont.io.wav.WavFileHeader
import io.github.lemcoder.mikrosoundfont.io.wav.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.exception.ApplicationStateException
import pl.lemanski.tc.utils.nativeRunCatching
import pl.lemanski.tc.utils.toNSData
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.addBoundaryTimeObserverForTimes
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSDataWritingAtomic
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.writeToFile
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun playAudio(data: FloatArray, sampleRate: Int) = suspendCancellableCoroutine { continuation ->
    val logger = Logger(AudioService::class)

    logger.debug("Writing data to file")
    val tmpFilePath = writeDataToTmpFile(data, sampleRate) ?: run {
        continuation.resumeWithException(ApplicationStateException("Error writing data to file"))
        return@suspendCancellableCoroutine
    }

    logger.debug("Setting up audio session")
    val audioSession = AVAudioSession.sharedInstance()

    nativeRunCatching {
        audioSession.setCategory(AVAudioSessionCategoryPlayback, it)

        onError {
            continuation.resumeWithException(ApplicationStateException("Error setting up audio session"))
        }
    }


    nativeRunCatching {
        audioSession.setActive(true, it)

        onError {
            continuation.resumeWithException(ApplicationStateException("Error setting up audio session"))
        }
    }

    logger.debug("Creating player")
    val item = AVPlayerItem(uRL = NSURL.fileURLWithPath(tmpFilePath))
    val avAudioPlayer: AVPlayer = AVPlayer.playerWithPlayerItem(item)

    logger.debug("Playing audio")
    avAudioPlayer.addBoundaryTimeObserverForTimes(
        listOf(CMTimeGetSeconds(item.duration)),
        null
    ) {
        logger.debug("Audio playback completed")
        deleteFile(tmpFilePath)
        continuation.resume(Unit)
    }
    avAudioPlayer.play()

    continuation.invokeOnCancellation {
        logger.debug("Audio playback cancelled")
        deleteFile(tmpFilePath)
        avAudioPlayer.pause()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun writeDataToTmpFile(data: FloatArray, sampleRate: Int): String? {
    val bytes = WavFileHeader.write(sampleRate.toUInt(), data.size.toUInt(), 2u).toByteArray()
    val audioData = (bytes + data.toByteArrayLittleEndian()).toNSData()
    val tempDirectoryPath = NSTemporaryDirectory()
    val fileManager = NSFileManager.defaultManager
    val tmpDirURL = NSURL.fileURLWithPath(tempDirectoryPath, isDirectory = true)
    val soundFileURL = tmpDirURL.URLByAppendingPathComponent("temp")?.URLByAppendingPathExtension("wav")

    nativeRunCatching {
        fileManager.createDirectoryAtURL(tmpDirURL, withIntermediateDirectories = false, attributes = null, error = it)
    }

    val filePath = soundFileURL?.path ?: throw IllegalStateException("File path is null")
    if (fileManager.fileExistsAtPath(filePath)) {
        deleteFile(filePath)
    }

    nativeRunCatching {
        audioData.writeToFile(filePath, NSDataWritingAtomic, it)

        onError {
            deleteFile(filePath)
        }
    }

    return filePath.takeIf { fileManager.fileExistsAtPath(it) }
}

@OptIn(ExperimentalForeignApi::class)
private fun deleteFile(filePath: String) {
    val fileManager = NSFileManager.defaultManager
    nativeRunCatching {
        fileManager.removeItemAtPath(filePath, it)
    }
}