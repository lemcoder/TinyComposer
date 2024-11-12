package pl.lemanski.tc.data.project

import pl.lemanski.tc.data.chord.encodeToString
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.buildMajorSeventh
import pl.lemanski.tc.domain.model.core.buildMinorTriad
import pl.lemanski.tc.domain.model.project.ChordBeats
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
            lengthInMeasures = 120,
            bpm = 100,
            rhythm = Rhythm.FOUR_FOURS,
            chords = listOf(
                ChordBeats(buildMinorTriad(Note(12)), 4),
                ChordBeats(buildMajorSeventh(Note(15)), 2)
            )
        )

        val expectedOutput = """
            id, name, lengthInBeats, bpm, rhythm, chords
            $projectId, Test Project, 120, 100, FOUR_FOURS, ${project.chords.encodeToString()}
        """.trimIndent() + "\n"

        assertEquals(expectedOutput, project.encodeToString())
    }

    @Test
    fun tryParseProject_should_correctly_deserialize_string_to_Project() {
        val projectId = UUID.random()
        val projectString = """
            id, name, lengthInBeats, bpm, rhythm, chords
            $projectId, Test Project, 120, 100, FOUR_FOURS, 12|min:4;15|maj7:2
        """.trimIndent()

        val expectedProject = Project(
            id = projectId,
            name = "Test Project",
            lengthInMeasures = 120,
            bpm = 100,
            rhythm = Rhythm.FOUR_FOURS,
            chords = listOf(
                ChordBeats(buildMinorTriad(Note(12)), 4),
                ChordBeats(buildMajorSeventh(Note(15)), 2)
            )
        )

        assertEquals(expectedProject, projectString.tryParseProject())
    }

    @Test
    fun tryParseProject_should_throw_exception_for_invalid_header() {
        val invalidHeaderString = """
            incorrect, header, format
            ${UUID.random()}, Test Project, 120, 100, SWING, 12|min:4;15|maj7:2
        """.trimIndent()

        assertFailsWith<IllegalArgumentException> {
            invalidHeaderString.tryParseProject()
        }
    }
}
