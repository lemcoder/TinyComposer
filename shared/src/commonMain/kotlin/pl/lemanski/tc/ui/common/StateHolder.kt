package pl.lemanski.tc.ui.common

import kotlinx.coroutines.flow.StateFlow

interface StateHolder<T> {
    val stateFlow: StateFlow<T>

    fun init()

    fun update(reducer: (T) -> T)
}