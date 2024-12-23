package pl.lemanski.tc.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import pl.lemanski.tc.ui.common.TcViewModel

/**
 * Interface representing a test scope for MVI (Model-View-Intent) ViewModel.
 *
 * @param VM The type of the ViewModel being tested, which extends MVIViewModel.
 * @param STATE The type of the state managed by the ViewModel.
 */
internal interface TCViewModelTestScope<VM : TcViewModel<STATE>, STATE> {

    /**
     * The last state emitted by the ViewModel.
     */
    val lastState: STATE

    /**
     * All states emitted by the ViewModel.
     */
    val allStates: List<STATE>

    /**
     * Performs an action on the ViewModel.
     * NOTE - This function should be used to perform SINGLE action!
     *
     * @param block The action to perform on the ViewModel.
     */
    fun performAction(block: VM.() -> Unit)

    /**
     * Retrieves the state at the specified index.
     *
     * @param index The index of the state to retrieve.
     * @return The state at the specified index.
     */
    fun state(index: Int): STATE
}

@OptIn(ExperimentalCoroutinesApi::class)
internal fun <VM : TcViewModel<STATE>, STATE> TestScope.testViewModel(viewModel: VM, block: TCViewModelTestScope<VM, STATE>.() -> Unit) = launch {

    val items = mutableListOf<STATE>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        viewModel.stateFlow.toList(items)
    }

    val scope = object : TCViewModelTestScope<VM, STATE> {
        override val lastState: STATE
            get() = items.last()

        override val allStates: List<STATE>
            get() = items

        override fun performAction(block: VM.() -> Unit) {
            with(viewModel) {
                block()
            }
            advanceUntilIdle()
        }

        override fun state(index: Int): STATE {
            return items[index]
        }
    }

    with(scope) {
        block()
    }
}