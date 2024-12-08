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
import pl.lemanski.tc.domain.model.core.changeOctave
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
import kotlin.math.abs

internal class ProjectDetailsViewModel(
    override val key: ProjectDetailsDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val getProjectUseCase: GetProjectUseCase,
    private val playbackControlUseCase: PlaybackControlUseCase,
    private val generateAudioUseCase: GenerateAudioUseCase
) : ProjectDetailsContract.ViewModel() {

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
        bottomSheet = null,
        tabComponent = StateComponent.TabComponent(
            selected = tabToOption(ProjectDetailsContract.Tab.CHORDS),
            options = ProjectDetailsContract.Tab.entries.map { tabToOption(it) }.toSet(),
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
                        chordBeats = project.chords.mapIndexed { index, chord -> chord.toChordBeatsComponent(index) }
                    )
                }
            }
            ProjectDetailsContract.Tab.MELODY -> {
                project = project.copy(melody = project.melody + NoteBeats(note, 1))

                _stateFlow.update { state ->
                    state.copy(
                        wheelPicker = null,
                        noteBeats = project.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) }
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

    //---

    override fun onBeatComponentClick(id: Int) {
        val isChordsTab = _stateFlow.value.tabComponent.selected.value == ProjectDetailsContract.Tab.CHORDS
        if (isChordsTab) {
            _stateFlow.update { state ->
                state.copy(bottomSheet = ChordBeatsBottomSheetStateHelper(id).initialState)
            }
        } else {
            _stateFlow.update { state ->
                state.copy(bottomSheet = NoteBeatsBottomSheetStateHelper(id).initialState)
            }
        }
    }

    override fun onBeatComponentLongClick(id: Int) {
        val isChordsTab = _stateFlow.value.tabComponent.selected.value == ProjectDetailsContract.Tab.CHORDS

        if (isChordsTab) {
            val newChords = project.chords.toMutableList()
            newChords.removeAt(id)

            project = project.copy(
                chords = newChords
            )

            _stateFlow.update { state ->
                state.copy(
                    chordBeats = project.chords.mapIndexed { index, chord -> chord.toChordBeatsComponent(index) }
                )
            }
        } else {
            val newMelody = project.melody.toMutableList()
            newMelody.removeAt(id)

            project = project.copy(
                melody = newMelody
            )

            _stateFlow.update { state ->
                state.copy(
                    noteBeats = project.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) }
                )
            }
        }
    }

    override fun onBeatComponentDoubleClick(id: Int) {
        val isChordsTab = _stateFlow.value.tabComponent.selected.value == ProjectDetailsContract.Tab.CHORDS

        // Clone
        if (isChordsTab) {
            val chordBeat = project.chords[id]
            val newChordBeats = project.chords.toMutableList()
            newChordBeats.add(id, chordBeat)

            project = project.copy(chords = newChordBeats)

            _stateFlow.update { state ->
                state.copy(
                    chordBeats = project.chords.mapIndexed { index, chord -> chord.toChordBeatsComponent(index) }
                )
            }
        } else {
            val noteBeat = project.melody[id]
            val newMelody = project.melody.toMutableList()
            newMelody.add(id, noteBeat)
            project = project.copy(melody = newMelody)

            _stateFlow.update { state ->
                state.copy(
                    noteBeats = project.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) }
                )
            }
        }
    }

    //---

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

    //---

    private fun tabToOption(tab: ProjectDetailsContract.Tab): StateComponent.TabComponent.Tab<ProjectDetailsContract.Tab> = StateComponent.TabComponent.Tab(
        name = when (tab) {
            ProjectDetailsContract.Tab.CHORDS -> i18n.projectDetails.chordsTab
            ProjectDetailsContract.Tab.MELODY -> i18n.projectDetails.melodyTab
        },
        value = tab
    )

    private fun getWheelPickerValues(): Set<String> = setOf("D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D") // FIXME

    private fun ChordBeats.toChordBeatsComponent(id: Int) = ProjectDetailsContract.State.ChordBeatsComponent(
        id = id,
        chordBeats = this,
        onChordClick = ::onBeatComponentClick,
        onChordDoubleClick = ::onBeatComponentDoubleClick,
        onChordLongClick = ::onBeatComponentLongClick
    )

    private fun NoteBeats.toNoteBeatsComponent(id: Int) = ProjectDetailsContract.State.NoteBeatsComponent(
        id = id,
        noteBeats = this,
        onNoteClick = ::onBeatComponentClick,
        onNoteDoubleClick = ::onBeatComponentDoubleClick,
        onNoteLongClick = ::onBeatComponentLongClick
    )

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

    //---

    inner class ChordBeatsBottomSheetStateHelper(
        private val id: Int,
    ) {
        private val chordBeats = project.chords[id]
        val initialState = ProjectDetailsContract.State.BottomSheet.ChordBottomSheet(
            chordBeatId = id,
            durationValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.duration,
                value = chordBeats.second,
                onValueChange = ::onDurationValueChange
            ),
            onDismiss = ::onDismiss
            // TODO add velocity
        )

        private fun onDismiss() {
            _stateFlow.update { state ->
                state.copy(
                    bottomSheet = null
                )
            }
        }

        fun onDurationValueChange(value: Int) {
            val (chord, _) = project.chords[id]
            val newChords = project.chords.toMutableList()
            val newChord = ChordBeats(chord, value)

            newChords[id] = newChord

            project = project.copy(
                chords = newChords
            )

            _stateFlow.update { state ->
                state.copy(
                    chordBeats = state.chordBeats.map { chordBeatsComponent ->
                        if (chordBeatsComponent.id == id) {
                            chordBeatsComponent.copy(
                                chordBeats = ChordBeats(chord, value)
                            )
                        } else {
                            chordBeatsComponent
                        }
                    },
                    bottomSheet = (state.bottomSheet as ProjectDetailsContract.State.BottomSheet.ChordBottomSheet).copy(
                        durationValuePicker = state.bottomSheet.durationValuePicker.copy(
                            value = value
                        )
                    )
                )
            }
        }
    }

    inner class NoteBeatsBottomSheetStateHelper(
        private val id: Int,
    ) {
        private val noteBeats = project.melody[id]
        val initialState = ProjectDetailsContract.State.BottomSheet.NoteBottomSheet(
            noteBeatId = id,
            octaveValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.octave,
                value = noteBeats.first.octave,
                onValueChange = ::onOctaveValueChange
            ),
            durationValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.duration,
                value = noteBeats.second,
                onValueChange = ::onDurationValueChange
            ),
            velocityValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.velocity,
                value = noteBeats.first.velocity,
                onValueChange = ::onVelocityValueChange
            ),
            onDismiss = ::onDismiss
        )

        private fun onDismiss() {
            _stateFlow.update { state ->
                state.copy(
                    bottomSheet = null
                )
            }
        }

        fun onOctaveValueChange(value: Int) {
            val (note, duration) = project.melody[id]
            val sign = if (value > note.octave) 1 else -1
            val delta = abs(value - note.octave) * sign
            val newNote = note.changeOctave(delta)

            val newMelody = project.melody.toMutableList()
            newMelody[id] = NoteBeats(newNote, duration)

            project = project.copy(
                melody = newMelody
            )

            _stateFlow.update { state ->
                state.copy(
                    noteBeats = project.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) },
                    bottomSheet = (state.bottomSheet as ProjectDetailsContract.State.BottomSheet.NoteBottomSheet).copy(
                        octaveValuePicker = state.bottomSheet.octaveValuePicker.copy(
                            value = value
                        )
                    )
                )
            }
        }

        fun onDurationValueChange(value: Int) {
            if (value !in 1..32) {
                return
            }

            val (note, _) = project.melody[id]
            val newMelody = project.melody.toMutableList()
            val newNote = NoteBeats(note, value)
            newMelody[id] = newNote

            project = project.copy(
                melody = newMelody
            )

            _stateFlow.update { state ->
                state.copy(
                    noteBeats = project.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) },
                    bottomSheet = (state.bottomSheet as ProjectDetailsContract.State.BottomSheet.NoteBottomSheet).copy(
                        durationValuePicker = state.bottomSheet.durationValuePicker.copy(
                            value = value
                        )
                    )
                )
            }
        }

        fun onVelocityValueChange(value: Int) {
            val (note, duration) = project.melody[id]
            val newMelody = project.melody.toMutableList()
            val newNote = note.copy(velocity = value)

            newMelody[id] = NoteBeats(newNote, duration)

            project = project.copy(
                melody = newMelody
            )

            _stateFlow.update { state ->
                state.copy(
                    noteBeats = project.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) },
                    bottomSheet = (state.bottomSheet as ProjectDetailsContract.State.BottomSheet.NoteBottomSheet).copy(
                        velocityValuePicker = state.bottomSheet.velocityValuePicker.copy(
                            value = value
                        )
                    )
                )
            }
        }
    }
}

