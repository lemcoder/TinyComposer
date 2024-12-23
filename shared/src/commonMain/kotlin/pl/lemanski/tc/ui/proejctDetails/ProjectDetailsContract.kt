package pl.lemanski.tc.ui.proejctDetails

import kotlinx.coroutines.Job
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectDetailsContract {
    enum class Tab {
        CHORDS,
        MELODY
    }

    abstract class ViewModel : TcViewModel<State>() {
        abstract fun onPlayButtonClicked(): Job
        abstract fun onStopButtonClicked(): Job
        abstract fun onAiGenerateButtonClicked()
        abstract fun onAddButtonClicked(): Job
        abstract fun onWheelPickerValueSelected(value: String)
        abstract fun onTabSelected(tab: Tab)
        abstract fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?)
        abstract fun hideSnackBar()
        abstract fun onProjectOptionsButtonClicked()
        abstract fun onBeatComponentClick(id: Int)
        abstract fun onBeatComponentLongClick(id: Int)
        abstract fun onBeatComponentDoubleClick(id: Int)
        abstract fun back()
    }

    data class State(
        val isLoading: Boolean,
        val projectName: String,
        val barLength: Int,
        val tabComponent: StateComponent.TabComponent<Tab>,
        val playButton: StateComponent.Button?,
        val stopButton: StateComponent.Button?,
        val backButton: StateComponent.Button,
        val addButton: StateComponent.Button,
        val projectDetailsButton: StateComponent.Button,
        val aiGenerateButton: StateComponent.Button,
        val wheelPicker: WheelPicker?,
        val noteBeats: List<NoteBeatsComponent>,
        val chordBeats: List<ChordBeatsComponent>,
        val bottomSheet: BottomSheet?,
        val snackBar: StateComponent.SnackBar?,
    ) {
        data class WheelPicker(
            val values: Set<String>,
            val selectedValue: String,
            val onValueSelected: (String) -> Unit,
        )

        data class ChordBeatsComponent(
            val id: Int,
            val isActive: Boolean,
            val chordBeats: ChordBeats,
            val onChordClick: (Int) -> Unit,
            val onChordDoubleClick: (Int) -> Unit,
            val onChordLongClick: (Int) -> Unit,
        )

        data class NoteBeatsComponent(
            val id: Int,
            val isActive: Boolean,
            val noteBeats: NoteBeats,
            val onNoteClick: (Int) -> Unit,
            val onNoteDoubleClick: (Int) -> Unit,
            val onNoteLongClick: (Int) -> Unit,
        )

        sealed interface BottomSheet {
            data class NoteBottomSheet(
                val noteBeatId: Int,
                val octaveValuePicker: StateComponent.ValuePicker,
                val durationValuePicker: StateComponent.ValuePicker,
                val velocityValuePicker: StateComponent.ValuePicker,
                val onDismiss: () -> Unit
            ) : BottomSheet

            data class ChordBottomSheet(
                val chordBeatId: Int,
                val durationValuePicker: StateComponent.ValuePicker,
                val chordTypeSelect: StateComponent.SelectInput<Chord.Type>,
                val octaveValuePicker: StateComponent.ValuePicker,
                val velocityValuePicker: StateComponent.ValuePicker,
                val onDismiss: () -> Unit
            ) : BottomSheet
        }
    }
}