package pl.lemanski.tc.domain.service.chord

import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.majorThird
import pl.lemanski.tc.domain.model.core.minorThird
import kotlin.test.Test
import kotlin.test.assertEquals


class ChordBuilderServiceTest {
    private var chordBuilderService: ChordBuilderService = ChordBuilderService()

    @Test
    fun buildMinorTriad_should_return_correct_notes() {
        val baseNote = Note(12) // assuming C is the base note
        chordBuilderService.setBaseNote(baseNote)

        val chord = chordBuilderService.buildMinorTriad()
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
        chordBuilderService.setBaseNote(baseNote)

        val chord = chordBuilderService.buildMajorTriad()
        val expectedNotes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().minorThird()
        )

        assertEquals("C", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }

    @Test
    fun buildDiminishedTriad_should_return_correct_notes() {
        val baseNote = Note(12)
        chordBuilderService.setBaseNote(baseNote)

        val chord = chordBuilderService.buildDiminishedTriad()
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
        chordBuilderService.setBaseNote(baseNote)

        val chord = chordBuilderService.buildAugmentedTriad()
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
        chordBuilderService.setBaseNote(baseNote)

        val chord = chordBuilderService.buildMinorSeventh()
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
        chordBuilderService.setBaseNote(baseNote)

        val chord = chordBuilderService.buildMajorSeventh()
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
        chordBuilderService.setBaseNote(baseNote)

        val chord = chordBuilderService.buildDominantSeventh()
        val expectedNotes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().minorThird(),
            baseNote.majorThird().minorThird().minorThird()
        )

        assertEquals("C7", chord.name)
        assertEquals(expectedNotes, chord.notes)
    }
}