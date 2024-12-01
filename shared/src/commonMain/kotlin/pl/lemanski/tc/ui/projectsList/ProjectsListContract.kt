package pl.lemanski.tc.ui.projectsList

import kotlinx.coroutines.Job
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel
import pl.lemanski.tc.utils.UUID

internal interface ProjectsListContract {
    abstract class ViewModel : TcViewModel<State>() {
        abstract fun onProjectClick(id: UUID): Job
        abstract fun onProjectDelete(id: UUID)
        abstract fun onAddButtonClick(): Job
        abstract fun showSnackBar(message: String, action: String? = null, onAction: (() -> Unit)? = null)
        abstract fun hideSnackBar()
    }

    data class State(
        val isLoading: Boolean,
        val title: String,
        val projectCards: List<ProjectCard>,
        val addButton: StateComponent.Button,
        val snackBar: StateComponent.SnackBar?
    ) {
        data class ProjectCard(
            val id: UUID,
            val name: String,
            val description: String,
            val onDelete: (UUID) -> Unit,
            val onClick: (UUID) -> Unit,
        )
    }
}