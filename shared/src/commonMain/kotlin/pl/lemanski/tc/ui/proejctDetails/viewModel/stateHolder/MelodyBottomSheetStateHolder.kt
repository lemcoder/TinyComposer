package pl.lemanski.tc.ui.proejctDetails.viewModel.stateHolder

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.domain.model.core.changeOctave
import pl.lemanski.tc.domain.useCase.loadProject.LoadProjectUseCase
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract
import pl.lemanski.tc.ui.proejctDetails.composable.melody.toNoteBeatsComponent
import pl.lemanski.tc.utils.UUID
import kotlin.math.abs

internal class MelodyBottomSheetStateHolder(
    private val noteIndex: Int,
    private val projectId: UUID,
    private val i18n: I18n,
    private val loadProjectUseCase: LoadProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val stateFlow: MutableStateFlow<ProjectDetailsContract.State>,
) {
    private val noteBeats = project.melody[noteIndex]
    val initialState = ProjectDetailsContract.State.BottomSheet.NoteBottomSheet(
        noteBeatId = noteIndex,
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
        if (value !in 1..8) {
            return
        }

        val (note, duration) = project.melody[noteIndex]
        val sign = if (value > note.octave) 1 else -1
        val delta = abs(value - note.octave) * sign
        val newNote = note.changeOctave(delta)

        val newMelody = project.melody.toMutableList()
        newMelody[noteIndex] = NoteBeats(newNote, duration)

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

        val (note, _) = project.melody[noteIndex]
        val newMelody = project.melody.toMutableList()
        val newNote = NoteBeats(note, value)
        newMelody[noteIndex] = newNote

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
        val (note, duration) = project.melody[noteIndex]
        val newMelody = project.melody.toMutableList()
        val newNote = note.copy(velocity = value)

        newMelody[noteIndex] = NoteBeats(newNote, duration)

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