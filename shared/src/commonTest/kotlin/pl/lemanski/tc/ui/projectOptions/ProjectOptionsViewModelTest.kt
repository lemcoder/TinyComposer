package pl.lemanski.tc.ui.projectOptions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

    // TODO move usecases to their own test classes
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
        val presets = mapOf(0 to "Preset 1", 1 to "Preset 2")

        override fun invoke(): Map<Int, String> {
            return presets
        }
    }


    private val presetsControlUseCase = object : PresetsControlUseCase {
        var lastMelodyPreset: Int? = null
        var lastChordPreset: Int? = null

        override fun getChordPreset(projectId: UUID): Int {
            return lastChordPreset ?: 0
        }

        override fun getMelodyPreset(projectId: UUID): Int {
            return lastMelodyPreset ?: 0
        }

        override fun setPresets(projectId: UUID, chordPreset: Int?, melodyPreset: Int?) {
            chordPreset?.let {
                lastChordPreset = it
            }

            melodyPreset?.let {
                lastMelodyPreset = it
            }
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
    fun test_initial_state() = runTest {
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
    fun test_onTempoChanged_with_valid_tempo() = runTest {
        val vm = viewModel

        vm.onTempoChanged("120")
        val state = vm.stateFlow.value
        assertNull(state.tempoInput.error)
        assertEquals("120", state.tempoInput.value)
    }

    @Test
    fun test_onTempoChanged_with_invalid_tempo() = runTest {
        val vm = viewModel

        vm.onTempoChanged("10")
        val state = vm.stateFlow.first()
        assertEquals(i18n.projectOptions.tempoError, state.tempoInput.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_onChordsPresetSelected() = runTest {
        val vm = viewModel

        val preset = vm.stateFlow.value.chordsPresetSelect.options.random()

        val values = mutableListOf<ProjectOptionsContract.State>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            vm.stateFlow.toList(values)
        }

        vm.onChordsPresetSelected(preset)
        advanceUntilIdle()

        assertEquals(preset, values.last().chordsPresetSelect.selected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_onMelodyPresetSelected() = runTest {
        val vm = viewModel

        val preset = vm.stateFlow.value.melodyPresetSelect.options.random()

        val values = mutableListOf<ProjectOptionsContract.State>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            vm.stateFlow.toList(values)
        }

        vm.onMelodyPresetSelected(preset)
        advanceUntilIdle()

        assertEquals(preset, values.last().melodyPresetSelect.selected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_note_and_chord_presets() = runTest {
        val vm = viewModel

        val chordPreset = vm.stateFlow.value.chordsPresetSelect.options.random()
        val melodyPreset = vm.stateFlow.value.melodyPresetSelect.options.random()

        vm.onMelodyPresetSelected(melodyPreset)
        advanceUntilIdle()
        assertEquals(melodyPreset.value, presetsControlUseCase.lastMelodyPreset)

        vm.onChordsPresetSelected(chordPreset)
        advanceUntilIdle()
        assertEquals(chordPreset.value, presetsControlUseCase.lastChordPreset)
    }

    @Test
    fun test_showSnackBar() = runTest {
        val vm = viewModel

        vm.showSnackBar("Test message", "Action", null)
        val state = vm.stateFlow.first()
        assertNotNull(state.snackBar)
        assertEquals("Test message", state.snackBar?.message)
        assertEquals("Action", state.snackBar?.action)
    }

    @Test
    fun test_hideSnackBar() = runTest {
        val vm = viewModel

        vm.showSnackBar("Test message", "Action", null)
        vm.hideSnackBar()
        val state = vm.stateFlow.first()
        assertNull(state.snackBar)
    }
}