package pl.lemanski.tc.domain.useCase.playbackControlUseCase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.service.audio.AudioService
import pl.lemanski.tc.utils.Logger

internal class PlaybackControlUseCaseImpl(
    private val audioService: AudioService
) : PlaybackControlUseCase {
    private val logger = Logger(this::class)
    private var isPlaying = false
    private var playbackJob: Job? = null
    private val playbackScope = CoroutineScope(Job() + Dispatchers.IO)

    override fun play(errorHandler: PlaybackControlUseCase.ErrorHandler, audioData: FloatArray) {
        logger.debug("Play audio")

        isPlaying = true
        playbackJob = playbackScope.launch {
            audioService.playAudio(audioData, 44_100)
        }
    }

    override suspend fun stop(errorHandler: PlaybackControlUseCase.ErrorHandler) {
        logger.debug("Stop audio")

        if (!isPlaying) {
            errorHandler.onControlStateError()
            return
        }

        playbackJob?.cancelAndJoin()
        audioService.stopAudio()
        isPlaying = false
    }
}