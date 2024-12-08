package pl.lemanski.tc.ui.proejctDetails

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.build
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.domain.service.navigation.goTo
import pl.lemanski.tc.domain.useCase.generateAudioUseCase.GenerateAudioUseCase
import pl.lemanski.tc.domain.useCase.getProject.GetProjectUseCase
import pl.lemanski.tc.domain.useCase.playbackControlUseCase.PlaybackControlUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.exception.ViewModelInitException

internal class ProjectDetailsViewModel(
    override val key: ProjectDetailsDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val getProjectUseCase: GetProjectUseCase,
    private val playbackControlUseCase: PlaybackControlUseCase,
    private val generateAudioUseCase: GenerateAudioUseCase
) : ProjectDetailsContract.ViewModel() {

    private val tabOptions = ProjectDetailsContract.Tab.entries.map { tabToOption(it) }.toSet()
    private val logger = Logger(this::class)
    private var project = getProjectUseCase(key.projectId) ?: throw ViewModelInitException("Project with id ${key.projectId} not found")
    private val initialState = ProjectDetailsContract.State(
        isLoading = true,
        projectName = project.name,
        playButton = StateComponent.Button(
            text = "",
            onClick = ::onPlayButtonClicked
        ),
        stopButton = null,
        backButton = StateComponent.Button(
            text = "",
            onClick = ::back
        ),
        aiGenerateButton = StateComponent.Button(
            text = "",
            onClick = ::onAiGenerateButtonClicked
        ),
        snackBar = null,
        wheelPicker = null,
        addButton = StateComponent.Button(
            text = "",
            onClick = ::onAddButtonClicked
        ),
        noteBeats = listOf(),
        chordBeats = listOf(),
        tabComponent = StateComponent.TabComponent(
            selected = tabOptions.first(),
            options = tabOptions,
            onTabSelected = ::onTabSelected
        ),
    )

    private val _stateFlow = MutableStateFlow(initialState)
    override val stateFlow: StateFlow<ProjectDetailsContract.State> = _stateFlow.asStateFlow()

    init {
        logger.debug("Init")
    }

    override fun onAttached() {
        logger.debug("Attached")

        _stateFlow.update { state ->
            state.copy(
                isLoading = false,
            )
        }
    }

    override fun onPlayButtonClicked(): Job = viewModelScope.launch {
        _stateFlow.update {
            it.copy(
                playButton = null,
                stopButton = StateComponent.Button(
                    text = "",
                    onClick = ::onStopButtonClicked
                )
            )
        }

        val audioData = generateAudioUseCase(GenerateAudioErrorHandler(), project.chords, project.melody, project.bpm)
        playbackControlUseCase.play(PlaybackControlErrorHandler(), audioData)
    }

    override fun onStopButtonClicked(): Job = viewModelScope.launch {
        playbackControlUseCase.stop(PlaybackControlErrorHandler())

        _stateFlow.update {
            it.copy(
                playButton = StateComponent.Button(text = "", onClick = ::onPlayButtonClicked),
                stopButton = null
            )
        }
    }

    override fun onAiGenerateButtonClicked() {
        viewModelScope.launch { // FIXME run synchronously
            navigationService.goTo(ProjectAiGenerateDestination(key.projectId))
        }
    }

    override fun onAddButtonClicked(): Job = viewModelScope.launch {
        _stateFlow.update { state ->
            state.copy(
                wheelPicker = ProjectDetailsContract.State.WheelPicker(
                    values = getWheelPickerValues(),
                    selectedValue = getWheelPickerValues().first(),
                    onValueSelected = ::onWheelPickerValueSelected
                )
            )
        }

    }

    override fun onWheelPickerValueSelected(value: String) {
        _stateFlow.update { state ->
            state.copy(
                wheelPicker = state.wheelPicker?.copy(
                    selectedValue = value
                )
            )
        }

        val note = Note.fromString(value) ?: run {
            showSnackBar(i18n.projectDetails.invalidNoteBeats, null, null)
            return
        }

        when (_stateFlow.value.tabComponent.selected.value) {
            ProjectDetailsContract.Tab.CHORDS -> {
                val chord = Chord.Type.MAJOR.build(note)
                project = project.copy(chords = project.chords + ChordBeats(chord, 1))

                _stateFlow.update { state ->
                    state.copy(
                        wheelPicker = null,
                        chordBeats = state.chordBeats + ProjectDetailsContract.State.ChordBeatsComponent(
                            id = state.chordBeats.size + 1,
                            chordBeats = ChordBeats(chord, 1),
                            onChordClick = {},
                            onChordDoubleClick = {},
                            onChordLongClick = {}
                        )
                    )
                }
            }
            ProjectDetailsContract.Tab.MELODY -> {
                project = project.copy(melody = project.melody + NoteBeats(note, 1))

                _stateFlow.update { state ->
                    state.copy(
                        wheelPicker = null,
                        noteBeats = state.noteBeats + ProjectDetailsContract.State.NoteBeatsComponent(
                            id = state.noteBeats.size + 1,
                            noteBeats = NoteBeats(note, 1),
                            onNoteClick = {},
                            onNoteDoubleClick = {},
                            onNoteLongClick = {}
                        )
                    )
                }
            }
        }
    }

    override fun onTabSelected(tab: ProjectDetailsContract.Tab) {
        _stateFlow.update { state ->
            state.copy(
                tabComponent = state.tabComponent.copy(
                    selected = tabToOption(tab),
                ),
                wheelPicker = null
            )
        }
    }

    override fun back() {
        // FIXME run synchronously
        viewModelScope.launch {
            navigationService.back()
        }
    }

    override fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?) {
        _stateFlow.update { state ->
            state.copy(
                snackBar = StateComponent.SnackBar(
                    message = message,
                    action = action,
                    onAction = onAction
                )
            )
        }
    }

    override fun hideSnackBar() {
        _stateFlow.update { state ->
            state.copy(
                snackBar = null
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        logger.debug("Cleared")
    }

    private fun tabToOption(tab: ProjectDetailsContract.Tab): StateComponent.TabComponent.Tab<ProjectDetailsContract.Tab> = StateComponent.TabComponent.Tab(
        name = when (tab) {
            ProjectDetailsContract.Tab.CHORDS -> i18n.projectDetails.chordsTab
            ProjectDetailsContract.Tab.MELODY -> i18n.projectDetails.melodyTab
        },
        value = tab
    )

    private fun getWheelPickerValues(): Set<String> = setOf("D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D") // FIXME

    //---

    inner class PlaybackControlErrorHandler : PlaybackControlUseCase.ErrorHandler {
        override fun onAudioDataNotInitialized() {
        }

        override fun onControlStateError() {
            logger.error("Control state error")
            hideSnackBar()
            showSnackBar(i18n.projectDetails.controlStateError, null, null)
        }
    }

    //---

    inner class GenerateAudioErrorHandler : GenerateAudioUseCase.ErrorHandler {
        override fun onInvalidChordBeats() {
            _stateFlow.update { state ->
                state.copy(
                    snackBar = StateComponent.SnackBar(
                        message = i18n.projectDetails.invalidChordBeats,
                        action = null,
                        onAction = null
                    )
                )
            }
        }

        override fun onInvalidNoteBeats() {
            _stateFlow.update { state ->
                state.copy(
                    snackBar = StateComponent.SnackBar(
                        message = i18n.projectDetails.invalidNoteBeats,
                        action = null,
                        onAction = null
                    )
                )
            }
        }
    }
}

