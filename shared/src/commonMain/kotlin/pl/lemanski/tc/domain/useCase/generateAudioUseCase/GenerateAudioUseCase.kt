package pl.lemanski.tc.domain.useCase.generateAudioUseCase

import pl.lemanski.tc.domain.model.project.ChordBeats

internal interface GenerateAudioUseCase {
    interface ErrorHandler {
        fun onInvalidChordBeats()
    }

    suspend operator fun invoke(
        errorHandler: ErrorHandler,
        chordBeats: List<ChordBeats>,
        tempo: Int
    ): FloatArray
}