package pl.lemanski.tc.ui.projectAiGenerate

import kotlinx.coroutines.Job
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectAiGenerateContract {

    enum class PromptOption {
        CHORDS_FOR_MELODY,
        MELODY_FOR_CHORDS,
        CHORDS,
        MELODY,
    }

    abstract class ViewModel : TcViewModel<State>() {
        abstract fun onSubmitClicked(): Job
        abstract fun onPromptInputChanged(input: String)
        abstract fun onPromptOptionSelected(option: StateComponent.RadioGroup.Option<PromptOption>)
        abstract fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?)
        abstract fun hideSnackBar()
        abstract fun back()
    }

    data class State(
        val isLoading: Boolean,
        val title: String,
        val promptOptions: StateComponent.RadioGroup<PromptOption>,
        val promptInput: StateComponent.Input,
        val submitButton: StateComponent.Button,
        val snackBar: StateComponent.SnackBar?,
    )
}