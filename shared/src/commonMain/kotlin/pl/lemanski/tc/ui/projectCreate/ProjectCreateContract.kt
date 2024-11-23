package pl.lemanski.tc.ui.projectCreate

import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.ui.common.LifecycleViewModel
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectCreateContract {
    abstract class ViewModel : TcViewModel<State>, LifecycleViewModel() {
        abstract fun onProjectNameInputChange(value: String)
        abstract fun onProjectBpmInputChange(value: String)
        abstract fun onProjectRhythmSelectChange(selected: StateComponent.SelectInput.Option<Rhythm>)
        abstract fun onCreateProjectClick()
        abstract fun clearErrors()
        abstract fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?)
        abstract fun hideSnackBar()
    }

    data class State(
        val isLoading: Boolean,
        val title: String,
        val projectName: StateComponent.Input,
        val projectBpm: StateComponent.Input,
        val projectRhythm: StateComponent.SelectInput<Rhythm>,
        val createProjectButton: StateComponent.Button,
        val errorSnackBar: StateComponent.SnackBar?
    )
}