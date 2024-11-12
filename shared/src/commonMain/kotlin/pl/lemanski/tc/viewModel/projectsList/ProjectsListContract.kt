package pl.lemanski.tc.viewModel.projectsList

import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.viewModel.StateComponent
import pl.lemanski.tc.viewModel.TcViewModel

interface ProjectsListContract {
    interface ViewModel : TcViewModel<State> {
        fun onProjectClick(id: UUID)
        fun onProjectLongClick(id: UUID)
        fun onAddButtonClick()
    }

    data class State(
        val isLoading: Boolean,
        val title: String,
        val projectCards: List<ProjectCard>,
        val addButton: StateComponent.Button
    ) {
        data class ProjectCard(
            val id: UUID,
            val name: String,
            val description: String,
            val onClick: () -> Unit,
        )
    }
}