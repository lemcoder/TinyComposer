package pl.lemanski.tc.ui.welcome

import kotlinx.coroutines.Job
import pl.lemanski.tc.ui.common.LifecycleViewModel
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel
import pl.lemanski.tc.utils.UUID

// TODO handle permissions and initialization here
internal interface WelcomeContract {
    abstract class ViewModel : TcViewModel<State>, LifecycleViewModel() {
        abstract fun goToProjectsList(): Job
    }

    data class State(
        val title: String,
    )
}