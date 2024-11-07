package pl.lemanski.tc.domain.model.core

import kotlin.test.Test
import kotlin.test.assertEquals

class ChordTest {
    @Test
    fun buildMinorTriad_should_return_correct_notes() {
        val baseNote = Note(12) // assuming C is the base note

        val chord = buildMinorTriad(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.minorThird(),
            baseNote.minorThird().majorThird()
        )

        assertEquals("Cmin", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildMajorTriad_should_return_correct_notes() {
        val baseNote = Note(12)

        val chord = buildMajorTriad(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().minorThird()
        )

        assertEquals("Cmaj", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildDiminishedTriad_should_return_correct_notes() {
        val baseNote = Note(12)

        val chord = buildDiminishedTriad(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.minorThird(),
            baseNote.minorThird().minorThird()
        )

        assertEquals("Cdim", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildAugmentedTriad_should_return_correct_notes() {
        val baseNote = Note(12)

        val chord = buildAugmentedTriad(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().majorThird()
        )

        assertEquals("Caug", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildMinorSeventh_should_return_correct_notes() {
        val baseNote = Note(12)

        val chord = buildMinorSeventh(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.minorThird(),
            baseNote.minorThird().majorThird(),
            baseNote.minorThird().majorThird().minorThird()
        )

        assertEquals("Cmin7", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildMajorSeventh_should_return_correct_notes() {
        val baseNote = Note(12)

        val chord = buildMajorSeventh(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().minorThird(),
            baseNote.majorThird().minorThird().majorThird()
        )

        assertEquals("Cmaj7", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildDominantSeventh_should_return_correct_notes() {
        val baseNote = Note(12)

        val chord = buildDominantSeventh(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().minorThird(),
            baseNote.majorThird().minorThird().minorThird()
        )

        assertEquals("C7", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildMinorSixth_should_return_correct_notes() {
        val baseNote = Note(12)

        val chord = buildMinorSixth(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.minorThird(),
            baseNote.minorThird().majorThird(), // Perfect fifth
            baseNote.minorThird().majorThird().majorSecond() // Major sixth
        )

        assertEquals("Cmin6", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildMajorSixth_should_return_correct_notes() {
        val baseNote = Note(12)

        val chord = buildMajorSixth(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().minorThird(), // Perfect fifth
            baseNote.majorThird().minorThird().majorSecond() // Major sixth
        )

        assertEquals("Cmaj6", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildDiminishedSeventh_should_return_correct_notes() {
        val baseNote = Note(12)

        val chord = buildDiminishedSeventh(baseNote)
        val expectedNotes = listOf(
            baseNote,
            baseNote.minorThird(),
            baseNote.minorThird().minorThird(), // Diminished fifth
            baseNote.minorThird().minorThird().minorThird() // Diminished seventh
        )

        assertEquals("Cdim7", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }
}