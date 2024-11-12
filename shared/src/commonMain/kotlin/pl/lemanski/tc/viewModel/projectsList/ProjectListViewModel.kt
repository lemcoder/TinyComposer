package pl.lemanski.tc.viewModel.projectsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.navigation.ProjectsDestination
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.i18n.I18n
import pl.lemanski.tc.viewModel.StateComponent

internal class ProjectListViewModel(
    private val key: ProjectsDestination,
    private val i18n: I18n,
    private val projectRepository: ProjectRepository
) : ProjectsListContract.ViewModel,
    ViewModel() {
    private val logger = Logger(this::class)

    private val _stateFlow = MutableStateFlow(
        ProjectsListContract.State(
            isLoading = true,
            title = i18n.projectList.title,
            projectCards = listOf(),
            addButton = StateComponent.Button(
                text = i18n.projectList.addProject,
                onClick = ::onAddButtonClick
            )
        )
    )

    override val stateFlow: StateFlow<ProjectsListContract.State> = _stateFlow.asStateFlow()

    override fun initialize() {
        logger.debug("Initialize")

        _stateFlow.update { state ->
            state.copy(isLoading = true)
        }

        val projects = projectRepository.getProjects()
        val projectCards = projects.map { project: Project ->
            ProjectsListContract.State.ProjectCard(
                id = project.id,
                name = project.name,
                description = "BPM: ${project.bpm} \n Duration: ${(project.lengthInMeasures * project.bpm) / 60}s",
                onClick = { onProjectClick(project.id) }
            )
        }

        _stateFlow.update { state ->
            state.copy(
                isLoading = false,
                projectCards = projectCards
            )
        }
    }

    override fun dispose(): Job = viewModelScope.launch {
        logger.debug("Dispose")
    } // Do nothing

    override fun onProjectClick(id: UUID) {
        logger.debug("Project clicked: $id")

    }

    override fun onProjectLongClick(id: UUID) {
        logger.debug("Project long clicked: $id")
    }

    override fun onAddButtonClick() {
        logger.debug("Add button clicked")
    }
}