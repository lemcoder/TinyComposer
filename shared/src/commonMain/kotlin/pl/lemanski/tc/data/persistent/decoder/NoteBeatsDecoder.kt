package pl.lemanski.tc.data.persistent.decoder

import pl.lemanski.tc.data.persistent.encoder.BEAT_DELIMITER
import pl.lemanski.tc.data.persistent.encoder.SEPARATOR
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.project.NoteBeats

internal fun String.tryDecodeNoteBeats(): List<NoteBeats> = when {
    isEmpty() -> emptyList()
    else      -> split(SEPARATOR).map {
        val (note, beat) = it.split(BEAT_DELIMITER)
        NoteBeats(Note(note.toInt()), beat.toInt())
    }
}
