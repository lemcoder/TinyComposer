package pl.lemanski.tc.data.chord

import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.song.ChordBeats

internal fun String.decode(): List<ChordBeats> = split(";").map {
    val (chordName, beat) = it.split(":")
    ChordBeats(chordName.toChord(), beat.toInt())
}

private fun String.toChord(): Chord {
    TODO("Not yet implemented")
}
