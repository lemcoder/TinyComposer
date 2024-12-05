package pl.lemanski.tc.domain.model.project

import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.utils.UUID

typealias ChordBeats = Pair<Chord, Int>
typealias NoteBeats = Pair<Note, Int>

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