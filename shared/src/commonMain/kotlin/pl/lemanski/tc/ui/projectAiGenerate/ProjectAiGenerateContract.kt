package pl.lemanski.tc.ui.projectAiGenerate

import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectAiGenerateContract {
    abstract class ViewModel : TcViewModel<State>() {
        abstract fun onSubmitClicked()
        abstract fun onPromptInputChanged(input: String)
        abstract fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?)
        abstract fun hideSnackBar()
        abstract fun back()
    }

    data class State(
        val isLoading: Boolean,
        val text: String,
        val promptInput: StateComponent.Input,
        val submitButton: StateComponent.Button,
        val backButton: StateComponent.Button,
        val snackBar: StateComponent.SnackBar?,
    )
}