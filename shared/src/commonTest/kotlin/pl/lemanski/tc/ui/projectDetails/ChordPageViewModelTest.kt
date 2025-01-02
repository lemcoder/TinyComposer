package pl.lemanski.tc.ui.projectDetails

import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.useCase.generateAudio.GenerateAudioUseCase
import pl.lemanski.tc.domain.useCase.loadProject.LoadProjectUseCase
import pl.lemanski.tc.domain.useCase.projectPresetsControl.PresetsControlUseCase
import pl.lemanski.tc.domain.useCase.saveProject.SaveProjectUseCase
import pl.lemanski.tc.domain.useCase.setMarkersUseCase.SetMarkersUseCase
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCase
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.ui.common.i18n.TestI18n
import pl.lemanski.tc.utils.UUID

class ChordPageViewModelTest {

    private val projectId = UUID.random()
    private val project = Project(
        id = projectId,
        name = "Test Project",
        bpm = 120,
        rhythm = Rhythm.FOUR_FOURS,
        chords = listOf(),
        melody = listOf()
    )

    private val i18n: I18n = TestI18n()
    private val navigationService = NavigationService()
    private val loadProjectUseCase = object : LoadProjectUseCase {
        override fun invoke(id: UUID): Project? {
            return project
        }
    }

    // TODO move to own class
    private val updateProjectUseCase = object : UpdateProjectUseCase {
        override fun invoke(errorHandler: UpdateProjectUseCase.ErrorHandler, project: Project, projectId: UUID): Project? {
            return project
        }
    }

    // TODO move to own class
    private val generateAudioUseCase = object : GenerateAudioUseCase {
        override suspend fun invoke(
            errorHandler: GenerateAudioUseCase.ErrorHandler,
            chordBeats: List<ChordBeats>,
            chordsPreset: Int,
            noteBeats: List<NoteBeats>,
            notesPreset: Int,
            tempo: Int,
            compingStyle: GenerateAudioUseCase.CompingStyle
        ): AudioStream {
            return AudioStream.EMPTY
        }
    }

    // TODO move to own class
    private val presetsControlUseCase = object : PresetsControlUseCase {
        override fun getChordPreset(projectId: UUID): Int {
            return 0
        }

        override fun getMelodyPreset(projectId: UUID): Int {
            return 0
        }

        override fun setPresets(projectId: UUID, chordPreset: Int?, melodyPreset: Int?) {}
    }

    // TODO move to own class
    private val saveProjectUseCase = object : SaveProjectUseCase {
        override fun invoke(errorHandler: SaveProjectUseCase.ErrorHandler, project: Project): Project? {
            return project
        }
    }

    // TODO move to own class
    private val setMarkersUseCase = object : SetMarkersUseCase {
        override fun invoke(project: Project, audioStream: AudioStream) {}
    }

    // TODO use cases tests first then view model tests~!
}