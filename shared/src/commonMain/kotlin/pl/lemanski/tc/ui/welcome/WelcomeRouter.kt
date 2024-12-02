package pl.lemanski.tc.ui.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.ui.common.localViewModel
import pl.lemanski.tc.ui.common.router

@Composable
fun WelcomeRouter() = router<WelcomeContract.ViewModel, WelcomeDestination> {
    val viewModel = localViewModel.current as WelcomeContract.ViewModel
    val state by viewModel.stateFlow.collectAsState()

    WelcomeScreen(
        title = state.title,
    )
}