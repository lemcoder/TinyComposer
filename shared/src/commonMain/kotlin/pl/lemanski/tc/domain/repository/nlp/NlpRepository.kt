package pl.lemanski.tc.domain.repository.nlp

import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats

internal interface NlpRepository {
    suspend fun generateChordBeats(prompt: String): List<ChordBeats>

    suspend fun generateMelody(prompt: String): List<NoteBeats>
}