package pl.lemanski.tc.domain.useCase.generateAudio

import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.model.project.CompingStyle

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
        tempo: Int,
        compingStyle: CompingStyle
    ): AudioStream
}