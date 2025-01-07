package pl.lemanski.tc.domain.model.audio

import io.github.lemcoder.mikrosoundfont.io.toByteArrayLittleEndian
import io.github.lemcoder.mikrosoundfont.io.wav.WavFileHeader
import io.github.lemcoder.mikrosoundfont.io.wav.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import pl.lemanski.tc.domain.service.audio.AudioService
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.nativeRunCatching
import pl.lemanski.tc.utils.toNSData
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.addBoundaryTimeObserverForTimes
import platform.AVFoundation.duration
import platform.AVFoundation.play
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSDataWritingAtomic
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class)
class AudioPlayer(
    private val data: FloatArray,
    private val channelCount: Int,
    private val sampleRate: Int,
) {
    @OptIn(ExperimentalForeignApi::class)
    internal fun play() {
        val logger = Logger(AudioService::class)

        logger.debug("Writing data to file")
        val tmpFilePath = writeDataToTmpFile(data, sampleRate) ?: run {
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
        val avAudioPlayer: AVPlayer = AVPlayer.playerWithPlayerItem(item)

        logger.debug("Playing audio")
        avAudioPlayer.addBoundaryTimeObserverForTimes(
            listOf(CMTimeGetSeconds(item.duration)),
            null
        ) {
            logger.debug("Audio playback completed")
            deleteFile(tmpFilePath)
        }
        avAudioPlayer.play()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun writeDataToTmpFile(data: FloatArray, sampleRate: Int): String? {
        val bytes = WavFileHeader.write(sampleRate.toUInt(), data.size.toUInt(), 1u).toByteArray()
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
}

internal actual fun AudioStream.play(isLoopingEnabled: Boolean) {
    try {
        val logger = Logger(AudioService::class)
        logger.debug("Init playing audio")

        val player = AudioPlayer(
            data = this.data,
            channelCount = this.output.value,
            sampleRate = AudioStream.SAMPLE_RATE
        )

        logger.debug("Playing audio")
        player.play()
    } catch (e: Exception) {
        println("Error playing audio: ${e.message}, ${e.stackTraceToString()}")
    }
}

internal actual fun AudioStream.stop() {
    val logger = Logger(AudioService::class)

    logger.debug("Stopping audio")
}