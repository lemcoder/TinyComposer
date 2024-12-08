package pl.lemanski.tc.data.repository.project

import pl.lemanski.tc.data.persistent.decoder.tryParseProject
import pl.lemanski.tc.data.persistent.encoder.encodeToString
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.buildMajorSeventh
import pl.lemanski.tc.domain.model.core.buildMinorTriad
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.utils.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class ProjectEnDecTest {
    @Test
    fun encodeToString_should_correctly_serialize_Project_to_string() {
        val projectId = UUID.random()
        val project = Project(
            id = projectId,
            name = "Test Project",
            bpm = 100,
            rhythm = Rhythm.FOUR_FOURS,
            chords = listOf(
                ChordBeats(buildMinorTriad(Note(12)), 4),
                ChordBeats(buildMajorSeventh(Note(15)), 2)
            ),
            melody = listOf(
                NoteBeats(Note(12), 4),
                NoteBeats(Note(15), 2)
            )
        )

        val expectedOutput = """
            id, name, bpm, rhythm, chords, melody
            $projectId, Test Project, 100, FOUR_FOURS, ${project.chords.encodeToString()}, ${project.melody.encodeToString()}
        """.trimIndent() + "\n"

        assertEquals(expectedOutput, project.encodeToString())
    }

    @Test
    fun tryParseProject_should_correctly_deserialize_string_to_Project() {
        val projectId = UUID.random()
        val projectString = """
            id, name, bpm, rhythm, chords, melody
            $projectId, Test Project, 100, FOUR_FOURS, 12.minV60:4;15.maj7V60:2, 12:4;15:2
        """.trimIndent()

        val expectedProject = Project(
            id = projectId,
            name = "Test Project",
            bpm = 100,
            rhythm = Rhythm.FOUR_FOURS,
            chords = listOf(
                ChordBeats(buildMinorTriad(Note(12)), 4),
                ChordBeats(buildMajorSeventh(Note(15)), 2)
            ),
            melody = listOf(
                NoteBeats(Note(12), 4),
                NoteBeats(Note(15), 2)
            )
        )

        assertEquals(expectedProject, projectString.tryParseProject())
    }

    @Test
    fun tryParseProject_should_throw_exception_for_invalid_header() {
        val invalidHeaderString = """
            incorrect, header, format
            ${UUID.random()}, Test Project, 120, 100, SWING, 12.minV60:4;15.maj7V60:2
        """.trimIndent()

        assertFailsWith<IllegalArgumentException> {
            invalidHeaderString.tryParseProject()
        }
    }
}
