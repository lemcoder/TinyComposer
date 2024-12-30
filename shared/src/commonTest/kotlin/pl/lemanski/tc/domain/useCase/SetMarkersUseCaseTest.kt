package pl.lemanski.tc.domain.useCase

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.build
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.domain.useCase.setMarkersUseCase.SetMarkersUseCaseImpl
import pl.lemanski.tc.utils.UUID
import kotlin.test.assertEquals

class SetMarkersUseCaseTest {
    private val cMin = Chord.Type.MINOR.build(Note(60))
    private val cMaj = Chord.Type.MAJOR.build(Note(60))

    private val mockAudioStream
        get() = AudioStream(
            data = FloatArray(60_000) { it.toFloat() },
            output = AudioStream.Output.MONO
        )

    private val mockProject
        get() = Project(
            id = UUID.random(),
            name = "Test",
            bpm = 60,
            rhythm = Rhythm.FOUR_FOURS,
            chords = listOf(
                cMin to 1,
                cMaj to 1
            ),
            melody = emptyList()
        )

    private val TestScope.testUseCase
        get() = SetMarkersUseCaseImpl()

    @Test
    fun should_place_markers_in_correct_points_of_audio_data() = runTest {
        val useCase = testUseCase
        val audiosStream = mockAudioStream
        val project = mockProject

        useCase.invoke(project, audiosStream)

        assertEquals(2, audiosStream.markers.size)
        assertEquals(0, audiosStream.markers[0].position)
        assertEquals((project.bpm / audiosStream.markers.size) * 1_000, audiosStream.markers[1].position)
    }
}