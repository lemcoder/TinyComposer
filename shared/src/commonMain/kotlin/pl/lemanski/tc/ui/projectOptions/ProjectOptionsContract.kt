package pl.lemanski.tc.ui.projectOptions

import kotlinx.coroutines.Job
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectOptionsContract {

    abstract class ViewModel : TcViewModel<State>() {
        abstract fun onTempoChanged(tempo: String)
        abstract fun onChordsPresetSelected(preset: StateComponent.SelectInput.Option<Int>): Job
        abstract fun onNotesPresetSelected(preset: StateComponent.SelectInput.Option<Int>): Job
        abstract fun onExportClicked()
        abstract fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?)
        abstract fun hideSnackBar()
        abstract fun back()
    }

    data class State(
        val isLoading: Boolean,
        val projectName: String,
        val tempoInput: StateComponent.Input,
        val chordsPresetSelect: StateComponent.SelectInput<Int>,
        val notesPresetSelect: StateComponent.SelectInput<Int>,
        val backButton: StateComponent.Button,
        val exportButton: StateComponent.Button,
        val snackBar: StateComponent.SnackBar?,
    )
}