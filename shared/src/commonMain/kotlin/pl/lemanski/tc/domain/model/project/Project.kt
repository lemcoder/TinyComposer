package pl.lemanski.tc.domain.model.project

import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.utils.UUID

data class Project(
    val id: UUID,
    val name: String,
    val bpm: Int,
    val rhythm: Rhythm,
    val chords: List<ChordBeats>,
    val melody: List<NoteBeats>
) {
    val lengthInMeasures: Int
        get() = chords.sumOf { it.second }
}