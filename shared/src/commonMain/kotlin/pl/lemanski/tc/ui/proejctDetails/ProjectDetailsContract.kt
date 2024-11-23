package pl.lemanski.tc.ui.proejctDetails

import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.ui.common.LifecycleViewModel
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectDetailsContract {
    abstract class ViewModel : TcViewModel<State>, LifecycleViewModel() {
    }

    data class State(
        val isLoading: Boolean,
        val title: String,
        val projectName: String,
        val projectBpm: String,
        val projectRhythm: String,
        val chordsTextArea: StateComponent.Input,
        val playButton: StateComponent.Button?,
        val stopButton: StateComponent.Button?,
        val aiGenerateButton: StateComponent.Button,
        val errorSnackBar: StateComponent.SnackBar?,
    )
}