package pl.lemanski.tc.ui.proejctDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.lemanski.tc.ui.common.router

@Composable
fun ProjectDetailsRouter() = router<ProjectDetailsContract.ViewModel> { viewModel ->
    val state by viewModel.stateFlow.collectAsState()

    ProjectDetailsScreen(
        isLoading = state.isLoading,
        title = state.title,
        projectName = state.projectName,
        projectBpm = state.projectBpm,
        projectRhythm = state.projectRhythm,
        chordsTextArea = state.chordsTextArea,
        playButton = state.playButton,
        stopButton = state.stopButton,
        aiGenerateButton = state.aiGenerateButton,
        errorSnackBar = state.errorSnackBar
    )
}