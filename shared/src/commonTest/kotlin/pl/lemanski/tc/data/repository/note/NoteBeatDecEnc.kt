package pl.lemanski.tc.data.repository.note

import pl.lemanski.tc.data.persistent.decoder.tryDecodeNoteBeats
import pl.lemanski.tc.data.persistent.encoder.encodeToString
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.project.NoteBeats
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class NoteBeatDecEncTest {

    @Test
    fun testEncodeToString() {
        val noteBeatsList = listOf(
            NoteBeats(Note(1), 2),
            NoteBeats(Note(3), 4)
        )
        val encodedString = noteBeatsList.encodeToString()
        assertEquals("1:2;3:4", encodedString)
    }

    @Test
    fun testTryDecodeNoteBeats() {
        val encodedString = "1:2;3:4"
        val decodedList = encodedString.tryDecodeNoteBeats()
        val expectedList = listOf(
            NoteBeats(Note(1), 2),
            NoteBeats(Note(3), 4)
        )
        assertEquals(expectedList, decodedList)
    }

    @Test
    fun testTryDecodeNoteBeatsEmptyString() {
        val encodedString = ""
        val decodedList = encodedString.tryDecodeNoteBeats()
        assertTrue(decodedList.isEmpty())
    }

    @Test
    fun testTryDecodeNoteBeatsInvalidFormat() {
        val encodedString = "invalid:string"
        assertFailsWith<NumberFormatException> {
            encodedString.tryDecodeNoteBeats()
        }
    }
}