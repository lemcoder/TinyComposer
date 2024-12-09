package pl.lemanski.tc.data.persistent.decoder

import pl.lemanski.tc.data.persistent.encoder.BEAT_DELIMITER
import pl.lemanski.tc.data.persistent.encoder.SEPARATOR
import pl.lemanski.tc.data.persistent.encoder.VELOCITY_DELIMITER
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.project.NoteBeats

internal fun String.tryDecodeNoteBeats(): List<NoteBeats> = when {
    isEmpty() -> emptyList()
    else      -> split(SEPARATOR).map {
        val (noteAndVelocity, beat) = it.split(BEAT_DELIMITER)
        val (note, velocity) = noteAndVelocity.split(VELOCITY_DELIMITER)
        NoteBeats(Note(note.toInt(), velocity.toInt()), beat.toInt())
    }
}
