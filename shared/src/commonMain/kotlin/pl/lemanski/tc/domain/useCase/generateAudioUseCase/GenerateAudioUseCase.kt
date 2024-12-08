package pl.lemanski.tc.domain.useCase.generateAudioUseCase

import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats

internal interface GenerateAudioUseCase {
    interface ErrorHandler {
        fun onInvalidChordBeats()
        fun onInvalidNoteBeats()
    }

    suspend operator fun invoke(
        errorHandler: ErrorHandler,
        chordBeats: List<ChordBeats>,
        noteBeats: List<NoteBeats>,
        tempo: Int
    ): FloatArray
}