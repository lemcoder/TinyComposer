package pl.lemanski.tc.ui.proejctDetails

import kotlinx.coroutines.Job
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectDetailsContract {
    abstract class ViewModel : TcViewModel<State>() {
        abstract fun onPlayButtonClicked(): Job
        abstract fun onStopButtonClicked(): Job
        abstract fun onAiGenerateButtonClicked()
        abstract fun onChordsTextAreaChanged(text: String)
        abstract fun onTempoInputChanged(tempo: String)
        abstract fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?)
        abstract fun hideSnackBar()
        abstract fun back()
    }

    data class State(
        val isLoading: Boolean,
        val projectName: String,
        val playButton: StateComponent.Button?,
        val stopButton: StateComponent.Button?,
        val backButton: StateComponent.Button,
        val aiGenerateButton: StateComponent.Button,
        val snackBar: StateComponent.SnackBar?,
        val noteBeats: List<NoteBeatsComponent>,
        val addNoteButton: StateComponent.Button,
        val chordBeats: List<ChordBeatsComponent>,
        val addChordButton: StateComponent.Button,
    ) {
        data class ChordBeatsComponent(
            val id: Int,
            val chordBeats: ChordBeats,
            val onChordClick: (Int) -> Unit,
            val onChordDoubleClick: (Int) -> Unit,
            val onChordLongClick: (Int) -> Unit,
        )

        data class NoteBeatsComponent(
            val id: Int,
            val noteBeats: NoteBeats,
            val onNoteClick: (Int) -> Unit,
            val onNoteDoubleClick: (Int) -> Unit,
            val onNoteLongClick: (Int) -> Unit,
        )
    }
}