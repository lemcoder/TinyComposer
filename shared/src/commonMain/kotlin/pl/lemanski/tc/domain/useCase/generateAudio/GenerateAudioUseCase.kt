package pl.lemanski.tc.domain.useCase.generateAudio

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
        chordsPreset: Int,
        noteBeats: List<NoteBeats>,
        notesPreset: Int,
        tempo: Int
    ): FloatArray
}