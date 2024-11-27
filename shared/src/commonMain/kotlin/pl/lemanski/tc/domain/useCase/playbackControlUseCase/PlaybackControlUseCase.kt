package pl.lemanski.tc.domain.useCase.playbackControlUseCase

internal interface PlaybackControlUseCase {
    interface ErrorHandler {
        fun onAudioDataNotInitialized()
        fun onControlStateError()
    }

    fun play(errorHandler: ErrorHandler, audioData: FloatArray)
    suspend fun stop(errorHandler: ErrorHandler)
}