package pl.lemanski.tc.data.persistent.encoder

import pl.lemanski.tc.domain.model.project.NoteBeats

internal fun List<NoteBeats>.encodeToString(): String = joinToString(SEPARATOR.toString()) { "${it.first.value}$VELOCITY_DELIMITER${it.first.velocity}$BEAT_DELIMITER${it.second}" }
