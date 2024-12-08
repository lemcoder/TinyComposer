package pl.lemanski.tc.domain.useCase.playbackControl

internal interface PlaybackControlUseCase {
    interface ErrorHandler {
        fun onAudioDataNotInitialized()
        fun onControlStateError()
    }

    suspend fun play(errorHandler: ErrorHandler, audioData: FloatArray)
}