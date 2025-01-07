package pl.lemanski.tc.ui.projectDetails

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectOptionsDestination
import pl.lemanski.tc.domain.model.project.CompingStyle
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
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract
import pl.lemanski.tc.ui.proejctDetails.viewModel.ProjectDetailsViewModel
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.testViewModel
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProjectDetailsViewModelTest {
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
            compingStyle: CompingStyle
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

    private val viewModel: ProjectDetailsViewModel
        get() = ProjectDetailsViewModel(
            ProjectDetailsDestination(projectId),
            i18n,
            navigationService,
            loadProjectUseCase,
            updateProjectUseCase,
            generateAudioUseCase,
            presetsControlUseCase,
            saveProjectUseCase,
            setMarkersUseCase
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
    }

    @Test
    fun test_initial_state() = runTest {
        val viewModel = viewModel
        assertEquals("Test Project", viewModel.stateFlow.value.projectName)
        assertTrue(viewModel.stateFlow.value.isLoading)
    }

    @Test
    fun test_onAttached() = runTest {
        val viewModel = viewModel
        viewModel.onAttached()
        assertFalse(viewModel.stateFlow.value.isLoading)
    }

    @Test
    @Ignore
    fun test_onPlayButtonClicked() = runTest {
        testViewModel(viewModel) {
            performAction {
                onPlayButtonClicked()
            }

            assertNull(lastState.playButton)
            assertNotNull(lastState.stopButton)
        }
    }

    @Test
    fun test_onStopButtonClicked() = runTest {
        testViewModel(viewModel) {
            performAction {
                onStopButtonClicked()
            }

            assertNotNull(lastState.playButton)
            assertNull(lastState.stopButton)
        }
    }

    @Test
    fun test_onAiGenerateButtonClicked() = runTest {
        testViewModel(viewModel) {
            performAction {
                onAiGenerateButtonClicked()
            }

            assertEquals(ProjectAiGenerateDestination(projectId), navigationService.history().last())
        }
    }

    @Test
    fun test_onTabSelected() = runTest {
        testViewModel(viewModel) {
            performAction {
                onTabSelected(ProjectDetailsContract.Tab.MELODY)
            }

            assertEquals(ProjectDetailsContract.Tab.MELODY, lastState.tabComponent.selected.value)
        }
    }

    @Test
    fun test_showSnackBar() = runTest {
        testViewModel(viewModel) {
            performAction {
                showSnackBar("Test message", "Action", null)
            }

            assertNotNull(lastState.snackBar)
            assertEquals("Test message", lastState.snackBar?.message)
        }
    }

    @Test
    fun test_hideSnackBar() = runTest {
        testViewModel(viewModel) {
            performAction {
                showSnackBar("Test message", "Action", null)
            }

            performAction {
                hideSnackBar()
            }

            assertNull(lastState.snackBar)
        }
    }

    @Test
    fun test_onProjectOptionsButtonClicked() = runTest {
        testViewModel(viewModel) {
            performAction {
                onProjectOptionsButtonClicked()
            }

            assertEquals(ProjectOptionsDestination(projectId), navigationService.history().last())
        }
    }
}