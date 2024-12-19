package pl.lemanski.tc.ui.projectOptions

import kotlinx.coroutines.Job
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectOptionsContract {

    abstract class ViewModel : TcViewModel<State>() {
        abstract fun onTempoChanged(tempo: String)
        abstract fun onChordsPresetSelected(preset: StateComponent.SelectInput.Option<Int>): Job
        abstract fun onMelodyPresetSelected(preset: StateComponent.SelectInput.Option<Int>): Job
        abstract fun onExportClicked()
        abstract fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?)
        abstract fun hideSnackBar()
        abstract fun back()
    }

    data class State(
        val isLoading: Boolean,
        val title: String,
        val tempoInput: StateComponent.Input,
        val chordsPresetSelect: StateComponent.SelectInput<Int>,
        val melodyPresetSelect: StateComponent.SelectInput<Int>,
        val exportButton: StateComponent.Button,
        val snackBar: StateComponent.SnackBar?,
    )
}