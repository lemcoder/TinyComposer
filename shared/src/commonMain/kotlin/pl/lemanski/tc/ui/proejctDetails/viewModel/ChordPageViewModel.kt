package pl.lemanski.tc.ui.proejctDetails.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.build
import pl.lemanski.tc.domain.model.core.changeOctave
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.useCase.loadProject.LoadProjectUseCase
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.exception.ViewModelInitException
import kotlin.math.abs

internal class ChordPageViewModel(
    private val projectId: UUID,
    private val viewModelScope: CoroutineScope,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val loadProjectUseCase: LoadProjectUseCase,
    private val projectDetailsViewModel: ProjectDetailsContract.ViewModel,
    private val i18n: I18n
) : ProjectDetailsContract.BaseViewModel by projectDetailsViewModel,
    ProjectDetailsContract.PageViewModel {

    private val initialProjectState
        get() = loadProjectOrThrow(projectId)
    private val logger = Logger(this::class)

    override fun onAddButtonClicked(): Job = viewModelScope.launch {
        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    wheelPicker = ProjectDetailsContract.State.PageState.WheelPicker(
                        values = getWheelPickerValues(),
                        selectedValue = getWheelPickerValues().first(),
                        onValueSelected = ::onWheelPickerValueSelected
                    )
                )
            )
        }
    }

    override fun onWheelPickerValueSelected(value: String) {
        val project = loadProjectOrThrow(projectId)
        val wheelPicker = mutableStateFlow.value.pageState.wheelPicker ?: run {
            logger.error("Wheel picker is null")
            return
        }

        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    wheelPicker = wheelPicker.copy(
                        selectedValue = value
                    )
                )
            )
        }

        val note = Note.fromString(value) ?: run {
            logger.error("Invalid note: $value")
            showSnackBar(i18n.projectDetails.invalidNoteBeats, null, null)
            return
        }

        val chord = Chord.Type.MAJOR.build(note)
        val newChords = project.chords + ChordBeats(chord, 1)
        val newProject = project.copy(chords = newChords)

        updateProjectUseCase(UpdateProjectUseCaseErrorHandler(), newProject, projectId)

        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    wheelPicker = null,
                    chordBeats = getChordComponents(newProject)
                )
            )
        }
    }

    override fun onAttached() {
        mutableStateFlow.update { state ->
            state.copy(
                pageState = ProjectDetailsContract.State.PageState(
                    addButton = StateComponent.Button(
                        text = "",
                        onClick = ::onAddButtonClicked
                    ),
                    barLength = initialProjectState.rhythm.beatsPerBar,
                    wheelPicker = null,
                    noteBeats = state.pageState.noteBeats, // TODO check
                    chordBeats = getChordComponents(initialProjectState),
                    bottomSheet = null
                )
            )
        }
    }

    //---

    override fun onBeatComponentClick(id: Int) {
        ChordBottomSheetStateHolder(id)
    }

    override fun onBeatComponentLongClick(id: Int) {
        val project = loadProjectOrThrow(projectId)

        val newChords = project.chords.toMutableList()
        newChords.removeAt(id)
        val newProject = project.copy(chords = newChords)
        updateProjectUseCase(
            errorHandler = UpdateProjectUseCaseErrorHandler(),
            project = newProject,
            projectId = projectId
        )

        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    chordBeats = getChordComponents(newProject)
                )
            )
        }
    }

    override fun onBeatComponentDoubleClick(id: Int) {
        val project = loadProjectOrThrow(projectId)

        val chordBeat = project.chords[id]
        val newChordBeats = project.chords.toMutableList().apply {
            add(id, chordBeat)
        }
        val newProject = project.copy(chords = newChordBeats)
        updateProjectUseCase(
            errorHandler = UpdateProjectUseCaseErrorHandler(),
            project = newProject,
            projectId = projectId
        )

        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    chordBeats = getChordComponents(newProject)
                )
            )
        }
    }

    // ---

    private fun getWheelPickerValues(): Set<String> = setOf("D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D") // FIXME

    private fun loadProjectOrThrow(projectId: UUID): Project {
        return loadProjectUseCase(projectId) ?: throw ViewModelInitException("Project with id $projectId not found")
    }

    private fun getChordComponents(project: Project): List<ProjectDetailsContract.State.PageState.ChordComponent> {
        return project.chords.flatMapIndexed { index, chord ->
            List(chord.second) { i ->
                ProjectDetailsContract.State.PageState.ChordComponent(
                    id = index,
                    isActive = false,
                    isPrimary = i == 0,
                    chord = chord.first,
                    onChordClick = ::onBeatComponentClick,
                    onChordDoubleClick = ::onBeatComponentDoubleClick,
                    onChordLongClick = ::onBeatComponentLongClick
                )
            }
        }
    }

    // ---

    inner class ChordBottomSheetStateHolder(
        private val chordIndex: Int,
    ) {
        private val logger: Logger = Logger(this::class)
        private val chordTypeOptions = Chord.Type.entries.map { chordType ->
            StateComponent.SelectInput.Option(
                name = chordType.name,
                value = chordType
            )
        }.toSet()

        private val chordBeats = loadProjectOrThrow(projectId).chords[chordIndex]
        private val initialState = ProjectDetailsContract.State.BottomSheet.ChordBottomSheet(
            chordBeatId = chordIndex,
            durationValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.duration,
                value = chordBeats.second,
                onValueChange = ::onDurationValueChange
            ),
            onDismiss = ::onDismiss,
            chordTypeSelect = StateComponent.SelectInput(
                selected = chordTypeOptions.find { it.value == chordBeats.first.type } ?: chordTypeOptions.first(),
                label = i18n.projectDetails.chordType,
                onSelected = ::onChordTypeSelected,
                options = chordTypeOptions
            ),
            octaveValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.octave,
                value = chordBeats.first.notes.first().octave,
                onValueChange = ::onOctaveValueChange
            ),
            velocityValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.velocity,
                value = chordBeats.first.velocity,
                onValueChange = ::onVelocityValueChange
            )
        )

        init {
            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        bottomSheet = initialState
                    ),
                )
            }
        }

        private fun onDismiss() {
            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        bottomSheet = null
                    ),
                )
            }
        }

        fun onDurationValueChange(value: Int) {
            val project = loadProjectOrThrow(projectId)
            val (chord, _) = project.chords[chordIndex]
            val newChords = project.chords.toMutableList()
            val newChord = ChordBeats(chord, value)

            newChords[chordIndex] = newChord
            val newProject = project.copy(chords = newChords)
            updateProjectUseCase(
                errorHandler = updateProjectErrorHandler,
                project = newProject,
                projectId = projectId
            )

            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        bottomSheet = (state.pageState.bottomSheet as ProjectDetailsContract.State.BottomSheet.ChordBottomSheet).copy(
                            durationValuePicker = state.pageState.bottomSheet.durationValuePicker.copy(
                                value = value
                            )
                        ),
                        chordBeats = getChordComponents(newProject)
                    ),
                )
            }
        }

        fun onChordTypeSelected(chordTypeOption: StateComponent.SelectInput.Option<Chord.Type>) {
            val project = loadProjectOrThrow(projectId)

            val chordType = chordTypeOption.value
            val (chord, duration) = project.chords[chordIndex]
            val newChords = project.chords.toMutableList()
            val newChord = ChordBeats(chordType.build(chord.notes.first()), duration)

            newChords[chordIndex] = newChord
            val newProject = project.copy(chords = newChords)
            updateProjectUseCase(
                errorHandler = updateProjectErrorHandler,
                project = newProject,
                projectId = projectId
            )

            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        bottomSheet = (state.pageState.bottomSheet as ProjectDetailsContract.State.BottomSheet.ChordBottomSheet).copy(
                            chordTypeSelect = state.pageState.bottomSheet.chordTypeSelect.copy(
                                selected = chordTypeOptions.find { it.value == chordType } ?: chordTypeOptions.first()
                            )
                        ),
                        chordBeats = getChordComponents(newProject)
                    )
                )
            }
        }

        fun onOctaveValueChange(value: Int) {
            val project = loadProjectOrThrow(projectId)

            if (value !in 1..8) {
                return
            }

            val (chord, duration) = project.chords[chordIndex]
            val sign = if (value > chord.notes.first().octave) 1 else -1
            val delta = abs(value - chord.notes.first().octave) * sign
            val newChord = chord.copy(notes = chord.notes.map { it.changeOctave(delta) })

            val newChords = project.chords.toMutableList()
            newChords[chordIndex] = ChordBeats(newChord, duration)
            val newProject = project.copy(chords = newChords)

            updateProjectUseCase(
                errorHandler = updateProjectErrorHandler,
                project = newProject,
                projectId = projectId
            )

            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        bottomSheet = (state.pageState.bottomSheet as ProjectDetailsContract.State.BottomSheet.ChordBottomSheet).copy(
                            octaveValuePicker = state.pageState.bottomSheet.octaveValuePicker.copy(
                                value = value
                            )
                        ),
                        chordBeats = getChordComponents(newProject)
                    )
                )
            }
        }

        fun onVelocityValueChange(value: Int) {
            val project = loadProjectOrThrow(projectId)
            val (chord, duration) = project.chords[chordIndex]
            val newChords = project.chords.toMutableList()
            val newChord = chord.copy(
                notes = chord.notes.map { it.copy(velocity = value) }
            )

            newChords[chordIndex] = ChordBeats(newChord, duration)
            val newProject = project.copy(chords = newChords)

            updateProjectUseCase(
                errorHandler = updateProjectErrorHandler,
                project = newProject,
                projectId = projectId
            )

            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        bottomSheet = (state.pageState.bottomSheet as ProjectDetailsContract.State.BottomSheet.ChordBottomSheet).copy(
                            velocityValuePicker = state.pageState.bottomSheet.velocityValuePicker.copy(
                                value = value
                            )
                        ),
                        chordBeats = getChordComponents(newProject)
                    )
                )
            }
        }

        private fun loadProjectOrThrow(projectId: UUID): Project {
            return loadProjectUseCase(projectId) ?: throw ViewModelInitException("Project with id $projectId not found")
        }

        // error handlers

        private val updateProjectErrorHandler = object : UpdateProjectUseCase.ErrorHandler {
            override fun onInvalidProjectName() {
                logger.warn("Invalid project name")
            }

            override fun onInvalidProjectBpm() {
                logger.warn("Invalid project bpm")
            }
        }
    }

    // ---

    inner class UpdateProjectUseCaseErrorHandler : UpdateProjectUseCase.ErrorHandler {
        override fun onInvalidProjectName() {
            logger.error("Invalid project name")
        }

        override fun onInvalidProjectBpm() {
            logger.error("Invalid project bpm")
        }
    }
}