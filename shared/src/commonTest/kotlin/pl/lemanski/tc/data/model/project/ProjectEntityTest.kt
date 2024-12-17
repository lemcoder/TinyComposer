package pl.lemanski.tc.data.model.project

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.lemanski.tc.data.model.core.ChordBeatsEntity
import pl.lemanski.tc.data.model.core.ChordEntity
import pl.lemanski.tc.data.model.core.NoteBeatsEntity
import pl.lemanski.tc.data.model.core.NoteEntity
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectEntityTest {

    @Test
    fun testProjectEntitySerialization() {
        val projectEntity = ProjectEntity(
            id = "123e4567-e89b-12d3-a456-426614174000",
            name = "Test Project",
            bpm = 120,
            rhythm = 0,
            chords = listOf(
                ChordBeatsEntity(
                    chord = ChordEntity(
                        type = "MAJOR",
                        baseNote = NoteEntity(60, 127)
                    ),
                    beats = 4
                )
            ),
            melody = listOf(
                NoteBeatsEntity(
                    note = NoteEntity(60, 127),
                    beats = 4
                )
            )
        )
        val json = Json.encodeToString(projectEntity)
        val expectedJson = """{"id":"123e4567-e89b-12d3-a456-426614174000","name":"Test Project","bpm":120,"rhythm":0,"chords":[{"chord":{"type":"MAJOR","baseNote":{"value":60,"velocity":127}},"beats":4}],"melody":[{"note":{"value":60,"velocity":127},"beats":4}]}"""
        assertEquals(expectedJson, json)
    }

    @Test
    fun testProjectEntityDeserialization() {
        val json = """{"id":"123e4567-e89b-12d3-a456-426614174000","name":"Test Project","bpm":120,"rhythm":0,"chords":[{"chord":{"type":"MAJOR","baseNote":{"value":60,"velocity":127}},"beats":4}],"melody":[{"note":{"value":60,"velocity":127},"beats":4}]}"""
        val projectEntity = Json.decodeFromString<ProjectEntity>(json)
        assertEquals("123e4567-e89b-12d3-a456-426614174000", projectEntity.id)
        assertEquals("Test Project", projectEntity.name)
        assertEquals(120, projectEntity.bpm)
        assertEquals(0, projectEntity.rhythm)
        assertEquals(1, projectEntity.chords.size)
        assertEquals("MAJOR", projectEntity.chords[0].chord.type)
        assertEquals(60, projectEntity.chords[0].chord.baseNote.value)
        assertEquals(127, projectEntity.chords[0].chord.baseNote.velocity)
        assertEquals(4, projectEntity.chords[0].beats)
        assertEquals(1, projectEntity.melody.size)
        assertEquals(60, projectEntity.melody[0].note.value)
        assertEquals(127, projectEntity.melody[0].note.velocity)
        assertEquals(4, projectEntity.melody[0].beats)
    }
}