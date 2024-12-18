package pl.lemanski.tc.ui.projectOptions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.lemanski.tc.domain.model.navigation.ProjectOptionsDestination
import pl.lemanski.tc.ui.common.localViewModel
import pl.lemanski.tc.ui.common.router

@Composable
fun ProjectOptionsRouter() = router<ProjectOptionsContract.ViewModel, ProjectOptionsDestination> {
    val viewModel = localViewModel.current as ProjectOptionsContract.ViewModel
    val state by viewModel.stateFlow.collectAsState()

    ProjectOptionsScreen(
        isLoading = state.isLoading,
        title = state.title,
        snackBar = state.snackBar,
        tempoInput = state.tempoInput,
        chordsPresetSelect = state.chordsPresetSelect,
        notesPresetSelect = state.notesPresetSelect,
        exportButton = state.exportButton,
    )
}