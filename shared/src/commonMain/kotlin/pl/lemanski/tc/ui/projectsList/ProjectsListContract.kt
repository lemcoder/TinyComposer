package pl.lemanski.tc.ui.projectsList

import kotlinx.coroutines.Job
import pl.lemanski.tc.ui.common.LifecycleViewModel
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.TcViewModel
import pl.lemanski.tc.utils.UUID

internal interface ProjectsListContract {
    abstract class ViewModel : TcViewModel<State>, LifecycleViewModel() {
        abstract fun onProjectClick(id: UUID)
        abstract fun onProjectLongClick(id: UUID)
        abstract fun onAddButtonClick(): Job
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