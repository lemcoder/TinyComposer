package pl.lemanski.tc.data.persistent.encoder

import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.project.ChordBeats

internal fun Chord.codeName(): String = "${this.notes.first().value}$CHORD_DELIMITER${type.notation}${VELOCITY_DELIMITER}${velocity}"

internal fun List<ChordBeats>.encodeToString(): String = joinToString(SEPARATOR.toString()) { "${it.first.codeName()}$BEAT_DELIMITER${it.second}" }
