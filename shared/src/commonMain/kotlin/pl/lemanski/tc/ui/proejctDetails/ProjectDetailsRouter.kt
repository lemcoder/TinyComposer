package pl.lemanski.tc.ui.proejctDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.ui.common.localViewModel
import pl.lemanski.tc.ui.common.router
import pl.lemanski.tc.ui.projectCreate.ProjectCreateContract

@Composable
fun ProjectDetailsRouter() = router<ProjectDetailsContract.ViewModel, ProjectDetailsDestination> {
    val viewModel = localViewModel.current as ProjectDetailsContract.ViewModel
    val state by viewModel.stateFlow.collectAsState()

    ProjectDetailsScreen(
        isLoading = state.isLoading,
        projectName = state.projectName,
        playButton = state.playButton,
        stopButton = state.stopButton,
        backButton = state.backButton,
        aiGenerateButton = state.aiGenerateButton,
        tabComponent = state.tabComponent,
        wheelPicker = state.wheelPicker,
        addButton = state.addButton,
        noteBeats = state.noteBeats,
        chordBeats = state.chordBeats,
        snackBar = state.snackBar,
        bottomSheet = state.bottomSheet,
    )
}