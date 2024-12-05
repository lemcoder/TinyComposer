package pl.lemanski.tc.domain.repository.genAi

import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats

internal interface GenAiRepository {
    suspend fun generateChordBeats(prompt: String): List<ChordBeats>

    suspend fun generateMelody(prompt: String): List<NoteBeats>
}