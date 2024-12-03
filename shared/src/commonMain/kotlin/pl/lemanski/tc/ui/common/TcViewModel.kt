package pl.lemanski.tc.ui.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import pl.lemanski.tc.domain.model.navigation.Destination

abstract class TcViewModel<STATE>: ViewModel() {
    abstract val key: Destination
    abstract val stateFlow: StateFlow<STATE>

    abstract fun onAttached()
}