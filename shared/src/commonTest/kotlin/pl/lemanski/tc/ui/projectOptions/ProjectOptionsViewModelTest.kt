package pl.lemanski.tc.ui.projectOptions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import pl.lemanski.tc.domain.model.navigation.ProjectOptionsDestination
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.goTo
import pl.lemanski.tc.domain.useCase.getSoundFontPresets.GetSoundFontPresetsUseCase
import pl.lemanski.tc.domain.useCase.loadProject.LoadProjectUseCase
import pl.lemanski.tc.domain.useCase.projectPresetsControl.ChordToNotePresets
import pl.lemanski.tc.domain.useCase.projectPresetsControl.PresetsControlUseCase
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.ui.common.i18n.TestI18n
import pl.lemanski.tc.utils.UUID
import kotlin.test.BeforeTest

class ProjectOptionsViewModelTest {

    private val projectId = UUID.random()
    private val project = Project(
        id = projectId,
        name = "Test",
        bpm = 60,
        rhythm = Rhythm.FOUR_FOURS,
        chords = listOf(),
        melody = listOf()
    )

    private val i18n: I18n = TestI18n()

    private val navigationService = NavigationService().apply {
        goTo(ProjectOptionsDestination(projectId))
    }

    private val loadProjectUseCase = object : LoadProjectUseCase {
        override fun invoke(id: UUID): Project? {
            return project
        }
    }

    private val updateProjectUseCase = object : UpdateProjectUseCase {
        override fun invoke(errorHandler: UpdateProjectUseCase.ErrorHandler, project: Project, projectId: UUID): Project? {
            return project
        }
    }

    private val getSoundFontPresetsUseCase = object : GetSoundFontPresetsUseCase {
        override fun invoke(): List<Pair<Int, String>> {
            return listOf(1 to "Preset 1", 2 to "Preset 2")
        }

    }

    private val presetsControlUseCase = object : PresetsControlUseCase {
        override fun getPresets(projectId: UUID): ChordToNotePresets {
            return 0 to 1
        }

        override fun setPresets(projectId: UUID, presets: ChordToNotePresets) {
            // no-op
        }
    }

    private val TestScope.viewModel: ProjectOptionsViewModel
        get() = ProjectOptionsViewModel(
            ProjectOptionsDestination(projectId),
            i18n,
            navigationService,
            loadProjectUseCase,
            updateProjectUseCase,
            getSoundFontPresetsUseCase,
            presetsControlUseCase
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
    }

    @Test
    fun `test initial state`() = runTest {
        val state = viewModel.stateFlow.first()
        assertFalse(state.isLoading)
        assertEquals(i18n.projectOptions.title, state.title)
        assertNull(state.snackBar)
        assertEquals(project.bpm.toString(), state.tempoInput.value)
        assertEquals(StateComponent.Input.Type.NUMBER, state.tempoInput.type)
        assertEquals(i18n.projectOptions.tempo, state.tempoInput.hint)
        assertNull(state.tempoInput.error)
    }

    @Test
    fun `test onTempoChanged with valid tempo`() = runTest {
        val vm = viewModel

        vm.onTempoChanged("120")
        val state = vm.stateFlow.value
        assertNull(state.tempoInput.error)
        assertEquals("120", state.tempoInput.value)
    }

    @Test
    fun `test onTempoChanged with invalid tempo`() = runTest {
        val vm = viewModel

        vm.onTempoChanged("10")
        val state = vm.stateFlow.first()
        assertEquals(i18n.projectOptions.tempoError, state.tempoInput.error)
    }

    @Test
    fun `test onChordsPresetSelected`() = runTest {
        val vm = viewModel

        val preset = StateComponent.SelectInput.Option("Preset 1", 0)
        vm.onChordsPresetSelected(preset)
        val state = vm.stateFlow.first()
        assertEquals(preset, state.chordsPresetSelect.selected)
    }

    @Test
    fun `test onNotesPresetSelected`() = runTest {
        val vm = viewModel

        val preset = StateComponent.SelectInput.Option("Preset 2", 1)
        vm.onNotesPresetSelected(preset)
        val state = vm.stateFlow.first()
        assertEquals(preset, state.notesPresetSelect.selected)
    }

    @Test
    fun `test showSnackBar`() = runTest {
        val vm = viewModel

        vm.showSnackBar("Test message", "Action", null)
        val state = vm.stateFlow.first()
        assertNotNull(state.snackBar)
        assertEquals("Test message", state.snackBar?.message)
        assertEquals("Action", state.snackBar?.action)
    }

    @Test
    fun `test hideSnackBar`() = runTest {
        val vm = viewModel

        vm.showSnackBar("Test message", "Action", null)
        vm.hideSnackBar()
        val state = vm.stateFlow.first()
        assertNull(state.snackBar)
    }
}