package pl.lemanski.tc.domain.useCase.playbackControlUseCase

import kotlinx.coroutines.Job

internal interface PlaybackControlUseCase {
    interface ErrorHandler {
        fun onAudioDataNotInitialized()
        fun onControlStateError()
    }

    suspend fun play(errorHandler: ErrorHandler, audioData: FloatArray)
}