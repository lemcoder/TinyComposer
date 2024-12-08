package pl.lemanski.tc.ui.projectOptions

import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectOptionsContract {

    abstract class ViewModel : TcViewModel<State>() {
        abstract fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?)
        abstract fun hideSnackBar()
        abstract fun back()
    }

    data class State(
        val isLoading: Boolean,
        val projectName: String,
        val backButton: StateComponent.Button,
        val snackBar: StateComponent.SnackBar?,
    )
}