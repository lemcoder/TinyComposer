package pl.lemanski.tc.domain.model.core

import pl.lemanski.tc.exception.InvalidNoteException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NoteTest {

    @Test
    fun throws_invalid_note_exception_for_values_out_of_range() {
        assertFailsWith<InvalidNoteException> { Note(-1) }
        assertFailsWith<InvalidNoteException> { Note(128) }
    }

    @Test
    fun returns_correct_name_and_octave_for_valid_note_values() {
        val note_c = Note(0)
        assertEquals("C", note_c.name)
        assertEquals(0, note_c.octave)

        val note_c_sharp = Note(1)
        assertEquals("C#", note_c_sharp.name)
        assertEquals(0, note_c_sharp.octave)

        val note_a4 = Note(57)  // Typically, A4 is the 57th MIDI note
        assertEquals("A", note_a4.name)
        assertEquals(5, note_a4.octave)
    }

    @Test
    fun adjusts_note_value_correctly_with_sharp_and_flat_methods() {
        val note_c = Note(0)
        val note_c_sharp = note_c.sharp()
        assertEquals(1, note_c_sharp.value)
        assertEquals("C#", note_c_sharp.name)

        val note_d_flat = Note(2).flat()
        assertEquals(1, note_d_flat.value)
        assertEquals("C#", note_d_flat.name)
    }

    @Test
    fun calculates_intervals_correctly_for_minor_and_major_seconds() {
        val note_c = Note(0)
        val minor_second = note_c.minorSecond()
        val major_second = note_c.majorSecond()

        assertEquals(1, minor_second.value)
        assertEquals("C#", minor_second.name)

        assertEquals(2, major_second.value)
        assertEquals("D", major_second.name)
    }

    @Test
    fun calculates_intervals_correctly_for_minor_and_major_thirds() {
        val note_c = Note(0)
        val minor_third = note_c.minorThird()
        val major_third = note_c.majorThird()

        assertEquals(3, minor_third.value)
        assertEquals("D#", minor_third.name)

        assertEquals(4, major_third.value)
        assertEquals("E", major_third.name)
    }
}