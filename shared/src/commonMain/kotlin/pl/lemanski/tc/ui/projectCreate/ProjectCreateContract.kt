package pl.lemanski.tc.ui.projectCreate

import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.ui.common.LifecycleViewModel
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel

internal interface ProjectCreateContract {
    abstract class ViewModel : TcViewModel<State>, LifecycleViewModel() {
        abstract fun onProjectNameInputChange(value: String)
        abstract fun onProjectBpmInputChange(value: String)
        abstract fun onProjectRhythmSelectChange(value: Rhythm)
        abstract fun onCreateProjectClick()
    }

    data class State(
        val isLoading: Boolean,
        val title: String,
        val projectName: StateComponent.Input,
        val projectBpm: StateComponent.Input,
        val projectRhythm: StateComponent.SelectInput<Rhythm>,
        val createProjectButton: StateComponent.Button
    )
}