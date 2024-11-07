package pl.lemanski.tc.data.chord

import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.project.ChordBeats

internal const val CHORD_DELIMITER: Char = '|'
internal const val BEAT_DELIMITER: Char = ':'
internal const val SEPARATOR: Char = ';'

internal fun Chord.codeName(): String = "${this.notes.first().value}$CHORD_DELIMITER${type.notation}"

internal fun List<ChordBeats>.encodeToString(): String = joinToString(SEPARATOR.toString()) { "${it.first.codeName()}$BEAT_DELIMITER${it.second}" }
