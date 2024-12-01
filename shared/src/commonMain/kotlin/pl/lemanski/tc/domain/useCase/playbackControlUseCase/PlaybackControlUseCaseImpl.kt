package pl.lemanski.tc.domain.useCase.playbackControlUseCase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.service.audio.playAudio
import pl.lemanski.tc.utils.Logger

internal class PlaybackControlUseCaseImpl : PlaybackControlUseCase {
    private val logger = Logger(this::class)
    private var isPlaying = false
    private var playbackJob: Job? = null
    private val playbackScope = CoroutineScope(Job() + Dispatchers.IO)

    override fun play(errorHandler: PlaybackControlUseCase.ErrorHandler, audioData: FloatArray) {
        logger.debug("Play audio")

        isPlaying = true
        playbackJob = playbackScope.launch {
            playAudio(audioData, 44_100)
        }
    }

    override suspend fun stop(errorHandler: PlaybackControlUseCase.ErrorHandler) {
        logger.debug("Stop audio")

        if (!isPlaying) {
            errorHandler.onControlStateError()
            return
        }

        playbackJob?.cancelAndJoin()
        isPlaying = false
    }
}