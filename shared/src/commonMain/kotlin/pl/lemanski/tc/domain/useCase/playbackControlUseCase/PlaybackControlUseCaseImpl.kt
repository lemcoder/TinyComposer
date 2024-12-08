package pl.lemanski.tc.domain.useCase.playbackControlUseCase

import kotlinx.coroutines.coroutineScope
import pl.lemanski.tc.domain.service.audio.playAudio
import pl.lemanski.tc.utils.Logger

internal class PlaybackControlUseCaseImpl : PlaybackControlUseCase {
    private val logger = Logger(this::class)

    override suspend fun play(errorHandler: PlaybackControlUseCase.ErrorHandler, audioData: FloatArray) = coroutineScope {
        logger.debug("Play audio")
        playAudio(audioData, 44_100)
    }
}