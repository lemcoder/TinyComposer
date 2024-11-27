package pl.lemanski.tc.ui.projectCreate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.ui.common.router

@Composable
fun ProjectCreateRouter() = router<ProjectCreateContract.ViewModel, ProjectCreateDestination> { viewModel ->
    val state by viewModel.stateFlow.collectAsState()

    ProjectCreateScreen(
        isLoading = state.isLoading,
        title = state.title,
        projectName = state.projectName,
        projectBpm = state.projectBpm,
        projectRhythm = state.projectRhythm,
        createProjectButton = state.createProjectButton
    )
}