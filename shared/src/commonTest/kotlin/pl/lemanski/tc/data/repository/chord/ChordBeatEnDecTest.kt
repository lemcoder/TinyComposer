package pl.lemanski.tc.data.repository.chord

import pl.lemanski.tc.data.persistent.encoder.codeName
import pl.lemanski.tc.data.persistent.encoder.encodeToString
import pl.lemanski.tc.data.persistent.decoder.toChord
import pl.lemanski.tc.data.persistent.decoder.tryDecodeChordBeats
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.buildMajorSeventh
import pl.lemanski.tc.domain.model.core.buildMajorTriad
import pl.lemanski.tc.domain.model.core.buildMinorTriad
import pl.lemanski.tc.domain.model.project.ChordBeats
import kotlin.test.Test
import kotlin.test.assertEquals

class ChordBeatEnDecTest {
    @Test
    fun encode_should_return_correct_string_representation_for_list_of_ChordBeats() {
        val chordBeatsList = listOf(
            ChordBeats(buildMinorTriad(Note(12, 60)), 1),
            ChordBeats(buildMajorSeventh(Note(15, 60)), 3)
        )

        val expectedEncoding = "12.minV60:1;15.maj7V60:3"
        assertEquals(expectedEncoding, chordBeatsList.encodeToString())
    }

    @Test
    fun tryDecodeChordBeats_should_return_correct_ChordBeats_list_from_encoded_string() {
        val encodedString = "12.minV60:1;15.maj7V60:3"

        val expectedChordBeatsList = listOf(
            ChordBeats(buildMinorTriad(Note(12, 60)), 1),
            ChordBeats(buildMajorSeventh(Note(15, 60)), 3)
        )

        assertEquals(expectedChordBeatsList, encodedString.tryDecodeChordBeats())
    }

    @Test
    fun tryDecodeChordBeats_should_return_empty_list_for_empty_string() {
        val encodedString = ""

        val expectedChordBeatsList = listOf<ChordBeats>()

        assertEquals(expectedChordBeatsList, encodedString.tryDecodeChordBeats())
    }

    @Test
    fun toChord_should_correctly_decode_single_chord_from_string() {
        val encodedChord = "12.minV127"
        val expectedChord = buildMinorTriad(Note(12))

        assertEquals(expectedChord, encodedChord.toChord())
    }

    @Test
    fun codeName_should_return_correct_encoded_name_for_Chord() {
        val chord = buildMajorTriad(Note(12))
        val expectedCodeName = "12.majV127"

        assertEquals(expectedCodeName, chord.codeName())
    }
}