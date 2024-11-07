package pl.lemanski.tc.domain.model.project

import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.utils.UUID

typealias ChordBeats = Pair<Chord, Int>

data class Project(
    val id: UUID,
    val name: String,
    val lengthInBeats: Int,
    val bpm: Int,
    val rhythm: Rhythm,
    val chords: List<ChordBeats>
)