package pl.lemanski.tc.data.model.core

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.Note
import kotlin.test.Test
import kotlin.test.assertEquals

class ChordEntityTest {

    @Test
    fun testChordEntitySerialization() {
        val chordEntity = ChordEntity(
            type = "MAJOR",
            baseNote = NoteEntity(60, 127)
        )
        val json = Json.encodeToString(chordEntity)
        val expectedJson = """{"type":"MAJOR","baseNote":{"value":60,"velocity":127}}"""
        assertEquals(expectedJson, json)
    }

    @Test
    fun testChordEntityDeserialization() {
        val json = """{"type":"MAJOR","baseNote":{"value":60,"velocity":127}}"""
        val chordEntity = Json.decodeFromString<ChordEntity>(json)
        assertEquals("MAJOR", chordEntity.type)
        assertEquals(60, chordEntity.baseNote.value)
        assertEquals(127, chordEntity.baseNote.velocity)
    }

    @Test
    fun testToDomain() {
        val chordEntity = ChordEntity(
            type = "MAJOR",
            baseNote = NoteEntity(60, 127)
        )
        val chord = chordEntity.toDomain()
        assertEquals(Chord.Type.MAJOR, chord.type)
        assertEquals(60, chord.notes.first().value)
        assertEquals(127, chord.notes.first().velocity)
    }

    @Test
    fun testToEntity() {
        val chord = Chord(
            type = Chord.Type.MAJOR,
            notes = listOf(
                Note(
                    value = 60,
                    velocity = 127
                )
            )
        )
        val chordEntity = chord.toEntity()
        assertEquals("MAJOR", chordEntity.type)
        assertEquals(60, chordEntity.baseNote.value)
        assertEquals(127, chordEntity.baseNote.velocity)
    }

    @Test
    fun testChordBeatsEntitySerialization() {
        val chordBeatsEntity = ChordBeatsEntity(
            chord = ChordEntity(
                type = "MAJOR",
                baseNote = NoteEntity(60, 127)
            ),
            beats = 4
        )
        val json = Json.encodeToString(chordBeatsEntity)
        val expectedJson = """{"chord":{"type":"MAJOR","baseNote":{"value":60,"velocity":127}},"beats":4}"""
        assertEquals(expectedJson, json)
    }

    @Test
    fun testChordBeatsEntityDeserialization() {
        val json = """{"chord":{"type":"MAJOR","baseNote":{"value":60,"velocity":127}},"beats":4}"""
        val chordBeatsEntity = Json.decodeFromString<ChordBeatsEntity>(json)
        assertEquals("MAJOR", chordBeatsEntity.chord.type)
        assertEquals(60, chordBeatsEntity.chord.baseNote.value)
        assertEquals(127, chordBeatsEntity.chord.baseNote.velocity)
        assertEquals(4, chordBeatsEntity.beats)
    }
}