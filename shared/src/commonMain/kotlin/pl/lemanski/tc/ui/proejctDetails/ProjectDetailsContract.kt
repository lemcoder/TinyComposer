package pl.lemanski.tc.ui.proejctDetails

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectDetailsContract {
    enum class Tab {
        CHORDS,
        MELODY
    }

    /**
     * Workaround for the fact that we can't have multiple inheritance in Kotlin.
     */
    interface BaseViewModel {
        val mutableStateFlow: MutableStateFlow<State>

        fun onPlayButtonClicked(): Job
        fun onStopButtonClicked(): Job
        fun onAiGenerateButtonClicked()
        fun onTabSelected(tab: Tab)
        fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?)
        fun hideSnackBar()
        fun onProjectOptionsButtonClicked()
        fun back()
    }

    interface PageViewModel {
        fun onBeatComponentClick(id: Int)
        fun onBeatComponentLongClick(id: Int)
        fun onBeatComponentDoubleClick(id: Int)
        fun onAddButtonClicked()
        fun onCloseButtonClicked()
        fun onWheelPickerValueSelected(value: String)
        fun onAttached()
    }

    abstract class ViewModel : TcViewModel<State>(), BaseViewModel

    data class State(
        val isLoading: Boolean,
        val projectName: String,
        val tabComponent: StateComponent.TabComponent<Tab>,
        val playButton: StateComponent.Button?,
        val stopButton: StateComponent.Button?,
        val backButton: StateComponent.Button,
        val projectDetailsButton: StateComponent.Button,
        val aiGenerateButton: StateComponent.Button,
        val pageState: PageState,
        val snackBar: StateComponent.SnackBar?,
    ) {
        data class PageState(
            val addButton: StateComponent.Button,
            val barLength: Int,
            val wheelPicker: WheelPicker?,
            val noteBeats: List<NoteComponent>,
            val chordBeats: List<ChordComponent>,
            val bottomSheet: BottomSheet?,
        ) {
            data class WheelPicker(
                val values: Set<String>,
                val selectedValue: String,
                val onValueSelected: (String) -> Unit,
            )

            data class ChordComponent(
                val id: Int,
                val isActive: Boolean,
                val isPrimary: Boolean,
                val chord: Chord,
                val onChordClick: (Int) -> Unit,
                val onChordDoubleClick: (Int) -> Unit,
                val onChordLongClick: (Int) -> Unit,
            )

            data class NoteComponent(
                val id: Int,
                val isActive: Boolean,
                val isPrimary: Boolean,
                val note: Note,
                val onNoteClick: (Int) -> Unit,
                val onNoteDoubleClick: (Int) -> Unit,
                val onNoteLongClick: (Int) -> Unit,
            )

            companion object {
                val EMPTY = PageState(
                    addButton = StateComponent.Button("") { },
                    barLength = 1,
                    wheelPicker = null,
                    noteBeats = emptyList(),
                    chordBeats = emptyList(),
                    bottomSheet = null
                )
            }
        }

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