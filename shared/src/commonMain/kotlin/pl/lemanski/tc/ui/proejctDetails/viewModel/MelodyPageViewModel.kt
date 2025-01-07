package pl.lemanski.tc.ui.proejctDetails.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.NoteBeats
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

internal class MelodyPageViewModel(
    private val projectId: UUID,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val loadProjectUseCase: LoadProjectUseCase,
    private val projectDetailsViewModel: ProjectDetailsContract.ViewModel,
    private val i18n: I18n
) : ProjectDetailsContract.BaseViewModel by projectDetailsViewModel,
    ProjectDetailsContract.PageViewModel {

    private val logger = Logger(this::class)

    override fun onAttached() {
        val initialProjectState = loadProjectOrThrow(projectId)

        mutableStateFlow.update { state ->
            state.copy(
                pageState = ProjectDetailsContract.State.PageState(
                    addButton = StateComponent.Button(
                        text = "",
                        onClick = ::onAddButtonClicked
                    ),
                    barLength = initialProjectState.rhythm.beatsPerBar,
                    wheelPicker = null,
                    noteBeats = getNoteComponents(initialProjectState),
                    chordBeats = state.pageState.chordBeats,
                    bottomSheet = null
                )
            )
        }
    }

    override fun onAddButtonClicked() {
        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    wheelPicker = ProjectDetailsContract.State.PageState.WheelPicker(
                        values = getWheelPickerValues(),
                        selectedValue = getWheelPickerValues().first(),
                        onValueSelected = ::onWheelPickerValueSelected
                    ),
                    addButton = state.pageState.addButton.copy(
                        onClick = ::onCloseButtonClicked
                    )
                )
            )
        }
    }

    override fun onCloseButtonClicked() {
        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    wheelPicker = null,
                    addButton = state.pageState.addButton.copy(
                        onClick = ::onAddButtonClicked
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

        val newNotes = project.melody.toMutableList().apply {
            add(NoteBeats(note, 1))
        }
        val newProject = project.copy(melody = newNotes)

        updateProjectUseCase(UpdateProjectUseCaseErrorHandler(), newProject, projectId)

        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    wheelPicker = null,
                    noteBeats = getNoteComponents(newProject)
                )
            )
        }
    }

    //---

    override fun onBeatComponentClick(id: Int) {
        NoteBottomSheetStateHolder(id)
    }

    override fun onBeatComponentLongClick(id: Int) {
        val project = loadProjectOrThrow(projectId)

        val newMelody = project.melody.toMutableList()
        newMelody.removeAt(id)
        val newProject = project.copy(melody = newMelody)
        updateProjectUseCase(
            errorHandler = UpdateProjectUseCaseErrorHandler(),
            project = newProject,
            projectId = projectId
        )

        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    noteBeats = getNoteComponents(newProject)
                )
            )
        }
    }

    override fun onBeatComponentDoubleClick(id: Int) {
        val project = loadProjectOrThrow(projectId)

        val noteBeat = project.melody[id]
        val newNoteBeats = project.melody.toMutableList().apply {
            add(id, noteBeat)
        }
        val newProject = project.copy(melody = newNoteBeats)
        updateProjectUseCase(
            errorHandler = UpdateProjectUseCaseErrorHandler(),
            project = newProject,
            projectId = projectId
        )

        mutableStateFlow.update { state ->
            state.copy(
                pageState = state.pageState.copy(
                    noteBeats = getNoteComponents(newProject)
                )
            )
        }
    }

    // ---

    private fun getWheelPickerValues(): Set<String> = setOf("D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D") // FIXME

    private fun loadProjectOrThrow(projectId: UUID): Project {
        return loadProjectUseCase(projectId) ?: throw ViewModelInitException("Project with id $projectId not found")
    }

    private fun getNoteComponents(project: Project): List<ProjectDetailsContract.State.PageState.NoteComponent> {
        return project.melody.flatMapIndexed { index, note ->
            List(note.second) { i ->
                ProjectDetailsContract.State.PageState.NoteComponent(
                    id = index,
                    isActive = false,
                    isPrimary = i == 0,
                    note = note.first,
                    onNoteClick = ::onBeatComponentClick,
                    onNoteDoubleClick = ::onBeatComponentDoubleClick,
                    onNoteLongClick = ::onBeatComponentLongClick
                )
            }
        }
    }

    // ---

    inner class NoteBottomSheetStateHolder(
        private val noteIndex: Int,
    ) {
        private val logger: Logger = Logger(this::class)

        private val noteBeats = loadProjectOrThrow(projectId).melody[noteIndex]
        private val initialState = ProjectDetailsContract.State.BottomSheet.NoteBottomSheet(
            noteBeatId = noteIndex,
            durationValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.duration,
                value = noteBeats.second,
                onValueChange = ::onDurationValueChange
            ),
            onDismiss = ::onDismiss,
            octaveValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.octave,
                value = noteBeats.first.octave,
                onValueChange = ::onOctaveValueChange
            ),
            velocityValuePicker = StateComponent.ValuePicker(
                label = i18n.projectDetails.velocity,
                value = noteBeats.first.velocity,
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
            val (notes, _) = project.melody[noteIndex]
            val newNotes = project.melody.toMutableList().apply {
                set(noteIndex, NoteBeats(notes, value))
            }

            val newProject = project.copy(melody = newNotes)
            updateProjectUseCase(
                errorHandler = updateProjectErrorHandler,
                project = newProject,
                projectId = projectId
            )

            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        bottomSheet = (state.pageState.bottomSheet as ProjectDetailsContract.State.BottomSheet.NoteBottomSheet).copy(
                            durationValuePicker = state.pageState.bottomSheet.durationValuePicker.copy(
                                value = value
                            )
                        ),
                        noteBeats = getNoteComponents(newProject)
                    ),
                )
            }
        }

        fun onOctaveValueChange(value: Int) {
            val project = loadProjectOrThrow(projectId)

            if (value !in 1..8) {
                return
            }

            val (note, duration) = project.melody[noteIndex]
            val sign = if (value > note.octave) 1 else -1
            val delta = abs(value - note.octave) * sign

            val newNotes = project.melody.toMutableList().apply {
                set(noteIndex, NoteBeats(note.changeOctave(delta), duration))
            }
            val newProject = project.copy(melody = newNotes)

            updateProjectUseCase(
                errorHandler = updateProjectErrorHandler,
                project = newProject,
                projectId = projectId
            )

            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        bottomSheet = (state.pageState.bottomSheet as ProjectDetailsContract.State.BottomSheet.NoteBottomSheet).copy(
                            octaveValuePicker = state.pageState.bottomSheet.octaveValuePicker.copy(
                                value = value
                            )
                        ),
                        noteBeats = getNoteComponents(newProject)
                    )
                )
            }
        }

        fun onVelocityValueChange(value: Int) {
            val project = loadProjectOrThrow(projectId)
            val (note, duration) = project.melody[noteIndex]
            val newNotes = project.melody.toMutableList().apply {
                set(noteIndex, NoteBeats(note.copy(velocity = value), duration))
            }
            val newProject = project.copy(melody = newNotes)

            updateProjectUseCase(
                errorHandler = updateProjectErrorHandler,
                project = newProject,
                projectId = projectId
            )

            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        bottomSheet = (state.pageState.bottomSheet as ProjectDetailsContract.State.BottomSheet.NoteBottomSheet).copy(
                            velocityValuePicker = state.pageState.bottomSheet.velocityValuePicker.copy(
                                value = value
                            )
                        ),
                        noteBeats = getNoteComponents(newProject)
                    )
                )
            }
        }

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