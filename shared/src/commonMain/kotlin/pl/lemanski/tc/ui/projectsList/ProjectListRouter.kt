package pl.lemanski.tc.ui.projectsList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.lemanski.tc.ui.common.router

@Composable
fun ProjectListRouter() = router<ProjectsListContract.ViewModel> { viewModel ->
    val state by viewModel.stateFlow.collectAsState()

    ProjectListScreen(
        isLoading = state.isLoading,
        title = state.title,
        projectCards = state.projectCards,
        addButton = state.addButton,
        snackBar = state.snackBar
    )
}