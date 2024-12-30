package pl.lemanski.tc.ui.proejctDetails.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.audio.play
import pl.lemanski.tc.domain.model.audio.stop
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.model.core.build
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectOptionsDestination
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.domain.service.navigation.goTo
import pl.lemanski.tc.domain.useCase.generateAudio.GenerateAudioUseCase
import pl.lemanski.tc.domain.useCase.loadProject.LoadProjectUseCase
import pl.lemanski.tc.domain.useCase.projectPresetsControl.PresetsControlUseCase
import pl.lemanski.tc.domain.useCase.saveProject.SaveProjectUseCase
import pl.lemanski.tc.domain.useCase.setMarkersUseCase.SetMarkersUseCase
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract
import pl.lemanski.tc.ui.proejctDetails.composable.chords.toChordComponents
import pl.lemanski.tc.ui.proejctDetails.composable.melody.toNoteBeatsComponent
import pl.lemanski.tc.ui.proejctDetails.viewModel.stateHolder.ChordBottomSheetStateHolder
import pl.lemanski.tc.ui.proejctDetails.viewModel.stateHolder.MelodyBottomSheetStateHolder
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.exception.ViewModelInitException

internal class ProjectDetailsViewModel(
    override val key: ProjectDetailsDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val loadProjectUseCase: LoadProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val generateAudioUseCase: GenerateAudioUseCase,
    private val presetsControlUseCase: PresetsControlUseCase,
    private val saveProjectUseCase: SaveProjectUseCase,
    private val setMarkersUseCase: SetMarkersUseCase
) : ProjectDetailsContract.ViewModel() {

    private var audioStream: AudioStream = AudioStream.EMPTY

    private val logger = Logger(this::class)
    private val initialProjectValue: Project = loadProjectOrThrow(key.projectId)
    private val initialState = ProjectDetailsContract.State(
        isLoading = true,
        projectName = initialProjectValue.name,
        barLength = initialProjectValue.rhythm.beatsPerBar,
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
        noteBeats = initialProjectValue.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) },
        chordBeats = initialProjectValue.chords.flatMapIndexed { index, chord -> chord.toChordComponents(index) }.mapIndexed { index, chordComponent ->
            chordComponent.copy(id = index)
        }, // FIXME
        bottomSheet = null,
        tabComponent = StateComponent.TabComponent(
            selected = tabToOption(ProjectDetailsContract.Tab.CHORDS),
            options = ProjectDetailsContract.Tab.entries.map { tabToOption(it) }.toSet(),
            onTabSelected = ::onTabSelected
        ),
        projectDetailsButton = StateComponent.Button(
            text = "",
            onClick = ::onProjectOptionsButtonClicked
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
        val project = loadProjectOrThrow(key.projectId)

        _stateFlow.update {
            it.copy(
                playButton = null,
                stopButton = StateComponent.Button(
                    text = "",
                    onClick = ::onStopButtonClicked
                )
            )
        }

        val isLoopingEnabled = true // TODO implement looping control
        val chordPreset = presetsControlUseCase.getChordPreset(project.id)
        val notePreset = presetsControlUseCase.getMelodyPreset(project.id)

        audioStream = generateAudioUseCase(
            errorHandler = GenerateAudioErrorHandler(),
            chordBeats = project.chords,
            chordsPreset = chordPreset,
            noteBeats = project.melody,
            notesPreset = notePreset,
            tempo = project.bpm
        )

        audioStream.onMarkerReached { marker ->
            logger.error("Marker reached: ${marker.index}")
            if (marker == AudioStream.END_MARKER) {
                _stateFlow.update {
                    it.copy(
                        playButton = StateComponent.Button(
                            text = "",
                            onClick = ::onPlayButtonClicked
                        ),
                        stopButton = null
                    )
                }
                audioStream.stop()
                return@onMarkerReached
            }

            val markerIndex = marker.index

            _stateFlow.update { state ->
                state.copy(
                    chordBeats = state.chordBeats.map {
                        it.copy(isActive = it.id == markerIndex)
                    },
                    noteBeats = state.noteBeats.map {
                        it.copy(isActive = it.id == markerIndex)
                    }

                )
            }
        }

        setMarkersUseCase(
            project = project,
            audioStream = audioStream
        )

        audioStream.play(isLoopingEnabled)
    }

    override fun onStopButtonClicked(): Job = viewModelScope.launch {
        audioStream.stop()

        _stateFlow.update {
            it.copy(
                playButton = StateComponent.Button(text = "", onClick = ::onPlayButtonClicked),
                stopButton = null
            )
        }
    }

    override fun onAiGenerateButtonClicked() {
        navigationService.goTo(ProjectAiGenerateDestination(key.projectId))
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
        val project = loadProjectOrThrow(key.projectId)
        val wheelPicker = _stateFlow.value.wheelPicker ?: run {
            logger.error("Wheel picker is null")
            return
        }

        _stateFlow.update { state ->
            state.copy(
                wheelPicker = wheelPicker.copy(
                    selectedValue = value
                )
            )
        }

        val note = Note.fromString(value) ?: run {
            logger.error("Invalid note: $value")
            showSnackBar(i18n.projectDetails.invalidNoteBeats, null, null)
            return
        }

        when (_stateFlow.value.tabComponent.selected.value) {
            ProjectDetailsContract.Tab.CHORDS -> {
                val chord = Chord.Type.MAJOR.build(note)
                val newChords = project.chords + ChordBeats(chord, 1)
                val newProject = project.copy(chords = newChords)

                updateProjectUseCase(UpdateProjectUseCaseErrorHandler(), newProject, key.projectId)

                _stateFlow.update { state ->
                    state.copy(
                        wheelPicker = null,
                        chordBeats = newProject.chords.flatMapIndexed { index, chord -> chord.toChordComponents(index) }
                    )
                }
            }
            ProjectDetailsContract.Tab.MELODY -> {
                val newNotes = project.melody + NoteBeats(note, 1)
                val newProject = project.copy(melody = newNotes)

                updateProjectUseCase(UpdateProjectUseCaseErrorHandler(), newProject, key.projectId)

                _stateFlow.update { state ->
                    state.copy(
                        wheelPicker = null,
                        noteBeats = newProject.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) }
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
            ChordBottomSheetStateHolder(
                chordIndex = id,
                projectId = key.projectId,
                i18n = i18n,
                stateFlow = _stateFlow,
                updateProjectUseCase = updateProjectUseCase,
                loadProjectUseCase = loadProjectUseCase
            )
        } else {
            MelodyBottomSheetStateHolder(
                noteIndex = id,
                projectId = key.projectId,
                i18n = i18n,
                stateFlow = _stateFlow,
                updateProjectUseCase = updateProjectUseCase,
                loadProjectUseCase = loadProjectUseCase
            )

        }
    }

    override fun onBeatComponentLongClick(id: Int) {
        val project = loadProjectOrThrow(key.projectId)
        val isChordsTab = _stateFlow.value.tabComponent.selected.value == ProjectDetailsContract.Tab.CHORDS

        if (isChordsTab) {
            val newChords = project.chords.toMutableList()
            newChords.removeAt(id)
            val newProject = project.copy(chords = newChords)
            updateProjectUseCase(
                errorHandler = UpdateProjectUseCaseErrorHandler(),
                project = newProject,
                projectId = key.projectId
            )

            _stateFlow.update { state ->
                state.copy(
                    chordBeats = newProject.chords.flatMapIndexed { index, chord -> chord.toChordComponents(index) }
                )
            }
        } else {
            val newMelody = project.melody.toMutableList()
            newMelody.removeAt(id)
            val newProject = project.copy(melody = newMelody)
            updateProjectUseCase(
                errorHandler = UpdateProjectUseCaseErrorHandler(),
                project = newProject,
                projectId = key.projectId
            )

            _stateFlow.update { state ->
                state.copy(
                    noteBeats = newProject.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) }
                )
            }
        }
    }

    override fun onBeatComponentDoubleClick(id: Int) {
        val project = loadProjectOrThrow(key.projectId)
        val isChordsTab = _stateFlow.value.tabComponent.selected.value == ProjectDetailsContract.Tab.CHORDS

        // Clone
        if (isChordsTab) {
            val chordBeat = project.chords[id]
            val newChordBeats = project.chords.toMutableList().apply {
                add(id, chordBeat)
            }
            val newProject = project.copy(chords = newChordBeats)
            updateProjectUseCase(
                errorHandler = UpdateProjectUseCaseErrorHandler(),
                project = newProject,
                projectId = key.projectId
            )

            _stateFlow.update { state ->
                state.copy(
                    chordBeats = project.chords.flatMapIndexed { index, chord -> chord.toChordComponents(index) }
                )
            }
        } else {
            val noteBeat = project.melody[id]
            val newMelody = project.melody.toMutableList().apply {
                add(id, noteBeat)
            }
            val newProject = project.copy(melody = newMelody)
            updateProjectUseCase(
                errorHandler = UpdateProjectUseCaseErrorHandler(),
                project = newProject,
                projectId = key.projectId
            )

            _stateFlow.update { state ->
                state.copy(
                    noteBeats = newProject.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) }
                )
            }
        }
    }

    //---

    override fun back() {
        navigationService.back()
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

    override fun onProjectOptionsButtonClicked() {
        navigationService.goTo(ProjectOptionsDestination(key.projectId))
    }

    override fun onCleared() {
        super.onCleared()

        val project = loadProjectOrThrow(key.projectId)
        saveProjectUseCase(SaveProjectUseCaseErrorHandler(), project)

        logger.debug("Cleared")
    }

    //---

    private fun getNoteComponents(): List<ProjectDetailsContract.State.NoteComponent> {
        val project = loadProjectOrThrow(key.projectId)
        return project.melody.mapIndexed { index, note -> note.toNoteBeatsComponent(index) }
    }

    private fun loadProjectOrThrow(projectId: UUID): Project {
        return loadProjectUseCase(projectId) ?: throw ViewModelInitException("Project with id $projectId not found")
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

    inner class UpdateProjectUseCaseErrorHandler : UpdateProjectUseCase.ErrorHandler {
        override fun onInvalidProjectName() {
            logger.error("Invalid project name")
        }

        override fun onInvalidProjectBpm() {
            logger.error("Invalid project bpm")
        }
    }

    //---

    inner class SaveProjectUseCaseErrorHandler : SaveProjectUseCase.ErrorHandler {
        override fun onInvalidProjectName() {
            // Will not happen
        }

        override fun onInvalidProjectBpm() {
            // Will not happen
        }

        override fun onProjectSaveError() {
            showSnackBar(i18n.projectDetails.projectSaveError, null, null)
        }
    }
}

