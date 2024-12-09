package pl.lemanski.tc.ui.projectAiGenerate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.ui.common.localViewModel
import pl.lemanski.tc.ui.common.router

@Composable
fun ProjectAiGenerateRouter() = router<ProjectAiGenerateContract.ViewModel, ProjectAiGenerateDestination> {
    val viewModel = localViewModel.current as ProjectAiGenerateContract.ViewModel
    val state by viewModel.stateFlow.collectAsState()

    ProjectAiGenerateScreen(
        isLoading = state.isLoading,
        title = state.title,
        promptOptions = state.promptOptions,
        promptInput = state.promptInput,
        submitButton = state.submitButton,
        backButton = state.backButton,
        text = state.text,
        snackBar = state.snackBar,
    )
}