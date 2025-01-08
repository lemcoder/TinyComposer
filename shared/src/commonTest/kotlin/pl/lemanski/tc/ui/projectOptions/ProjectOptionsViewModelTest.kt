package pl.lemanski.tc.ui.projectOptions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
import pl.lemanski.tc.utils.testViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

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
        testViewModel(viewModel) {
            assertFalse(lastState.isLoading)
            assertEquals(i18n.projectOptions.title, lastState.title)
            assertNull(lastState.snackBar)
            assertEquals(project.bpm.toString(), lastState.tempoInput.value)
            assertEquals(StateComponent.Input.Type.NUMBER, lastState.tempoInput.type)
            assertEquals(i18n.projectOptions.tempo, lastState.tempoInput.hint)
            assertNull(lastState.tempoInput.error)
        }
    }

    @Test
    fun test_onTempoChanged_with_valid_tempo() = runTest {
        testViewModel(viewModel) {
            performAction {
                onTempoChanged("120")
            }

            assertNull(lastState.tempoInput.error)
            assertEquals("120", lastState.tempoInput.value)
        }

    }

    @Test
    fun test_onTempoChanged_with_invalid_tempo() = runTest {
        testViewModel(viewModel) {
            performAction {
                onTempoChanged("abc")
            }

            assertEquals(i18n.projectOptions.tempoError, lastState.tempoInput.error)
        }
    }

    @Test
    fun test_onChordsPresetSelected() = runTest {
        testViewModel(viewModel) {
            val preset = lastState.chordsPresetSelect.options.random()

            performAction {
                onChordsPresetSelected(preset)
            }

            assertEquals(preset, lastState.chordsPresetSelect.selected)
        }
    }

    @Test
    fun test_onMelodyPresetSelected() = runTest {
        testViewModel(viewModel) {
            val preset = lastState.melodyPresetSelect.options.random()

            performAction {
                onMelodyPresetSelected(preset)
            }

            assertEquals(preset, lastState.melodyPresetSelect.selected)
        }
    }

    @Test
    fun test_note_and_chord_presets() = runTest {
        testViewModel(viewModel) {
            val chordPreset = lastState.chordsPresetSelect.options.random()
            val melodyPreset = lastState.melodyPresetSelect.options.random()

            performAction {
                onMelodyPresetSelected(melodyPreset)
            }
            assertEquals(melodyPreset.value, presetsControlUseCase.lastMelodyPreset)

            performAction {
                onChordsPresetSelected(chordPreset)
            }

            assertEquals(chordPreset.value, presetsControlUseCase.lastChordPreset)
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
            assertEquals("Action", lastState.snackBar?.action)
        }
    }

    @Test
    fun test_hideSnackBar() = runTest {
        testViewModel(viewModel) {
            performAction {
                showSnackBar("Test message", "Action", null)
            }

            assertNotNull(lastState.snackBar)

            performAction {
                hideSnackBar()
            }

            assertNull(lastState.snackBar)
        }
    }
}