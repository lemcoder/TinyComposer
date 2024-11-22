package pl.lemanski.tc.ui.projectsList

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectsDestination
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.goTo
import pl.lemanski.tc.domain.service.navigation.key
import pl.lemanski.tc.domain.useCase.deleteProject.DeleteProjectUseCaseErrorHandler
import pl.lemanski.tc.domain.useCase.deleteProject.deleteProjectUseCase
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCaseErrorHandler
import pl.lemanski.tc.domain.useCase.getProjectsList.getProjectsListUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.exception.NavigationStateException

internal class ProjectListViewModel(
    private val i18n: I18n,
    private val navigationService: NavigationService
) : ProjectsListContract.ViewModel() {

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

    override val key: ProjectsDestination = navigationService.key<ProjectsDestination>() ?: throw NavigationStateException("Key not found")
    override val stateFlow: StateFlow<ProjectsListContract.State> = _stateFlow.asStateFlow()

    override fun initialize() {
        logger.debug("Initialize")

        _stateFlow.update { state ->
            state.copy(isLoading = true)
        }

        val projects = getProjectsListUseCase()
        val projectCards = projects.map { project: Project ->
            ProjectsListContract.State.ProjectCard(
                id = project.id,
                name = project.name,
                description = "BPM: ${project.bpm}\n${i18n.projectList.duration}: ${(project.lengthInMeasures * project.bpm) / 60}s",
                onDelete = ::onProjectDelete,
                onClick = ::onProjectClick
            )
        }

        _stateFlow.update { state ->
            state.copy(
                isLoading = false,
                projectCards = projectCards
            )
        }
    }

    override fun onProjectDelete(id: UUID) {
        logger.debug("Project delete clicked: $id")

        _stateFlow.update { state ->
            state.copy(isLoading = true)
        }

        val project = deleteProjectUseCase(DeleteProjectErrorHandler()) {
            id
        }

        if (project != null) {
            val projectCards = stateFlow.value.projectCards.filter { it.id != id }

            _stateFlow.update { state ->
                state.copy(
                    isLoading = false,
                    projectCards = projectCards
                )
            }
        }
    }

    override fun onProjectClick(id: UUID) {
        logger.debug("Project clicked: $id")

    }

    override fun onProjectLongClick(id: UUID) {
        logger.debug("Project long clicked: $id")
    }

    override fun onAddButtonClick(): Job = viewModelScope.launch {
        navigationService.goTo(ProjectCreateDestination)
    }

    //---

    inner class DeleteProjectErrorHandler : DeleteProjectUseCaseErrorHandler {
        override fun handleDeleteProjectError() {
            logger.error("Error while deleting project")
        }
    }
}