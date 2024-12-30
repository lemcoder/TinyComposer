package pl.lemanski.tc.ui.proejctDetails.viewModel.stateHolder

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.ChordBeats
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


internal class ChordBottomSheetStateHolder(
    private val chordIndex: Int,
    private val projectId: UUID,
    private val i18n: I18n,
    private val loadProjectUseCase: LoadProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val stateFlow: MutableStateFlow<ProjectDetailsContract.State>,
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
        stateFlow.update { state ->
            state.copy(
                bottomSheet = initialState
            )
        }
    }

    private fun onDismiss() {
        stateFlow.update { state ->
            state.copy(
                bottomSheet = null
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

        stateFlow.update { state ->
            state.copy(
                bottomSheet = (state.bottomSheet as ProjectDetailsContract.State.BottomSheet.ChordBottomSheet).copy(
                    durationValuePicker = state.bottomSheet.durationValuePicker.copy(
                        value = value
                    )
                )
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

        stateFlow.update { state ->
            state.copy(
                bottomSheet = (state.bottomSheet as ProjectDetailsContract.State.BottomSheet.ChordBottomSheet).copy(
                    chordTypeSelect = state.bottomSheet.chordTypeSelect.copy(
                        selected = chordTypeOptions.find { it.value == chordType } ?: chordTypeOptions.first()
                    )
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

        stateFlow.update { state ->
            state.copy(
                bottomSheet = (state.bottomSheet as ProjectDetailsContract.State.BottomSheet.ChordBottomSheet).copy(
                    octaveValuePicker = state.bottomSheet.octaveValuePicker.copy(
                        value = value
                    )
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

        stateFlow.update { state ->
            state.copy(
                bottomSheet = (state.bottomSheet as ProjectDetailsContract.State.BottomSheet.ChordBottomSheet).copy(
                    velocityValuePicker = state.bottomSheet.velocityValuePicker.copy(
                        value = value
                    )
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
