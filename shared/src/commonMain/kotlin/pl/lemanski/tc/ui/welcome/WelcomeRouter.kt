package pl.lemanski.tc.ui.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.lemanski.tc.ui.common.router

@Composable
fun WelcomeRouter() = router<WelcomeContract.ViewModel> { viewModel ->
    val state by viewModel.stateFlow.collectAsState()

    WelcomeScreen(
        title = state.title,
    )
}