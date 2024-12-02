package pl.lemanski.tc.ui.projectsList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.ui.common.localViewModel
import pl.lemanski.tc.ui.common.router
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract

@Composable
fun ProjectListRouter() = router<ProjectsListContract.ViewModel, ProjectListDestination> {
    val viewModel = localViewModel.current as ProjectsListContract.ViewModel
    val state by viewModel.stateFlow.collectAsState()

    ProjectListScreen(
        isLoading = state.isLoading,
        title = state.title,
        projectCards = state.projectCards,
        addButton = state.addButton,
        snackBar = state.snackBar
    )
}