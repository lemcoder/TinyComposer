package pl.lemanski.tc.data.model.core

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.lemanski.tc.domain.model.core.Note
import kotlin.test.Test
import kotlin.test.assertEquals

class NoteEntityTest {

    @Test
    fun testSerialization() {
        val noteEntity = NoteEntity(value = 60, velocity = 127)
        val json = Json.encodeToString(noteEntity)
        val expectedJson = """{"value":60,"velocity":127}"""
        assertEquals(expectedJson, json)
    }

    @Test
    fun testDeserialization() {
        val json = """{"value":60,"velocity":127}"""
        val noteEntity = Json.decodeFromString<NoteEntity>(json)
        assertEquals(60, noteEntity.value)
        assertEquals(127, noteEntity.velocity)
    }

    @Test
    fun testToDomain() {
        val noteEntity = NoteEntity(value = 60, velocity = 127)
        val note = noteEntity.toDomain()
        assertEquals(60, note.value)
        assertEquals(127, note.velocity)
    }

    @Test
    fun testToEntity() {
        val note = Note(
            value = 60,
            velocity = 127
        )
        val noteEntity = note.toEntity()
        assertEquals(60, noteEntity.value)
        assertEquals(127, noteEntity.velocity)
    }

    @Test
    fun testNoteBeatsEntitySerialization() {
        val noteBeatsEntity = NoteBeatsEntity(
            note = NoteEntity(value = 60, velocity = 127),
            beats = 4
        )
        val json = Json.encodeToString(noteBeatsEntity)
        val expectedJson = """{"note":{"value":60,"velocity":127},"beats":4}"""
        assertEquals(expectedJson, json)
    }

    @Test
    fun testNoteBeatsEntityDeserialization() {
        val json = """{"note":{"value":60,"velocity":127},"beats":4}"""
        val noteBeatsEntity = Json.decodeFromString<NoteBeatsEntity>(json)
        assertEquals(60, noteBeatsEntity.note.value)
        assertEquals(127, noteBeatsEntity.note.velocity)
        assertEquals(4, noteBeatsEntity.beats)
    }
}