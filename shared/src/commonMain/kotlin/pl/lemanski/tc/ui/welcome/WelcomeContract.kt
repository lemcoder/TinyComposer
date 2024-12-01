package pl.lemanski.tc.ui.welcome

import kotlinx.coroutines.Job
import pl.lemanski.tc.ui.common.TcViewModel

// TODO handle permissions and initialization here
internal interface WelcomeContract {
    abstract class ViewModel : TcViewModel<State>() {
        abstract fun goToProjectsList(): Job
    }

    data class State(
        val title: String,
    )
}