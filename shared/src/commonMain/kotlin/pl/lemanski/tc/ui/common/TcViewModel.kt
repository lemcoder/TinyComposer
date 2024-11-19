package pl.lemanski.tc.ui.common

import kotlinx.coroutines.flow.StateFlow
import pl.lemanski.tc.domain.model.navigation.Destination

interface TcViewModel<STATE> {
    val key: Destination
    val stateFlow: StateFlow<STATE>

    fun initialize()
}