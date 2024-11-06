package pl.lemanski.tc.domain.model.song

import pl.lemanski.tc.domain.model.core.Chord

typealias ChordBeats = Pair<Chord, Int>

data class Song(
    private val length: Int,
    private val bpm: Int,
    private val rhythm: Rhythm,
    private val chords: List<ChordBeats>
)