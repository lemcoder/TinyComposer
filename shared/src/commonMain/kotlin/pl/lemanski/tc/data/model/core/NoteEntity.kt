package pl.lemanski.tc.data.model.core

import kotlinx.serialization.Serializable
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.NoteBeats

@Serializable
internal data class NoteBeatsEntity(
    val note: NoteEntity,
    val beats: Int
)

internal fun NoteBeatsEntity.toDomain(): NoteBeats = note.toDomain() to beats

internal fun NoteBeats.toEntity(): NoteBeatsEntity = NoteBeatsEntity(
    note = first.toEntity(),
    beats = second
)

@Serializable
internal data class NoteEntity(
    val value: Int,
    val velocity: Int
)

internal fun NoteEntity.toDomain(): Note = Note(
    value = value,
    velocity = velocity
)

internal fun Note.toEntity(): NoteEntity = NoteEntity(
    value = value,
    velocity = velocity
)