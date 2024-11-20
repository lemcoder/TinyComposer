package pl.lemanski.tc.data.persistent.decoder

import pl.lemanski.tc.data.persistent.encoder.BEAT_DELIMITER
import pl.lemanski.tc.data.persistent.encoder.CHORD_DELIMITER
import pl.lemanski.tc.data.persistent.encoder.SEPARATOR
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.Chord.Type
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.build
import pl.lemanski.tc.domain.model.project.ChordBeats

internal fun String.tryDecodeChordBeats(): List<ChordBeats> = when {
    isEmpty() -> emptyList()
    else      -> split(SEPARATOR).map {
        val (chordName, beat) = it.split(BEAT_DELIMITER)
        ChordBeats(chordName.toChord(), beat.toInt())
    }
}


internal fun String.toChord(): Chord {
    val (baseNote, typeNotation) = split(CHORD_DELIMITER, limit = 2)
    val type = Type.entries.first { it.notation == typeNotation }
    return type.build(Note(baseNote.toInt()))
}
