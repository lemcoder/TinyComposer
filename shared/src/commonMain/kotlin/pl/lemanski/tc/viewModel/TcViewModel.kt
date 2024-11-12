package pl.lemanski.tc.viewModel

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

interface TcViewModel<STATE> {
    val stateFlow: StateFlow<STATE>

    fun initialize()
    fun dispose(): Job
}