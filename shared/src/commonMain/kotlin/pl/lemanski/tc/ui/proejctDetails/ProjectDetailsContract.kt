package pl.lemanski.tc.ui.proejctDetails

import kotlinx.coroutines.Job
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
        val projectRhythm: String,
        val tempoInput: StateComponent.Input,
        val chordsTextArea: StateComponent.Input,
        val playButton: StateComponent.Button?,
        val stopButton: StateComponent.Button?,
        val backButton: StateComponent.Button,
        val aiGenerateButton: StateComponent.Button,
        val snackBar: StateComponent.SnackBar?,
    )
}