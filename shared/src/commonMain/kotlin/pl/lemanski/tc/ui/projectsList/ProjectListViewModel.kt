package pl.lemanski.tc.ui.projectsList

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.build
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.goTo
import pl.lemanski.tc.domain.useCase.saveProject.SaveProjectUseCase
import pl.lemanski.tc.domain.useCase.deleteProject.DeleteProjectUseCase
import pl.lemanski.tc.domain.useCase.getProjectsList.GetProjectsListUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.round

internal class ProjectListViewModel(
    override val key: ProjectListDestination,
    private val i18n: I18n,
    private val saveProjectUseCase: SaveProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getProjectsListUseCase: GetProjectsListUseCase,
    private val navigationService: NavigationService
) : ProjectsListContract.ViewModel() {

    private val logger = Logger(this::class)
    private val initialState = ProjectsListContract.State(
        isLoading = true,
        title = i18n.projectList.title,
        projectCards = listOf(),
        addButton = StateComponent.Button(
            text = i18n.projectList.addProject,
            onClick = ::onAddButtonClick
        ),
        snackBar = null,
        noProjectsText = i18n.projectList.noProjects,
        loadSampleProjectsButton = StateComponent.Button(
            text = i18n.projectList.loadSampleProjects,
            onClick = ::onLoadSampleProjectsClick
        )
    )

    private val _stateFlow = MutableStateFlow(initialState)
    override val stateFlow: StateFlow<ProjectsListContract.State> = _stateFlow.asStateFlow()

    init {
        logger.debug("Initialize")
    }

    override fun onAttached() {
        logger.debug("Attached")
        _stateFlow.update { state ->
            state.copy(isLoading = true)
        }

        val projects = getProjectsListUseCase()

        _stateFlow.update { state ->
            state.copy(
                isLoading = false,
                projectCards = projects.map(::mapProjectToProjectCard)
            )
        }

        hideSnackBar()
    }

    override fun onProjectDelete(id: UUID) {
        logger.debug("Project delete clicked: $id")

        _stateFlow.update { state ->
            state.copy(
                isLoading = true,
            )
        }

        hideSnackBar()

        val project = deleteProjectUseCase(DeleteProjectErrorHandler(), id)

        if (project == null) {
            showSnackBar(i18n.projectList.projectDeleteFailed)
        } else {
            val projectPosition = stateFlow.value.projectCards.indexOfFirst { it.id == id }
            val projectCards = stateFlow.value.projectCards.filter { it.id != id }

            _stateFlow.update { state ->
                state.copy(
                    projectCards = projectCards,
                )
            }

            showSnackBar(
                message = i18n.projectList.projectDeleted,
                action = i18n.common.undo
            ) {
                val recreatedProject = saveProjectUseCase(CreateProjectErrorHandler(), project)
                if (recreatedProject == null) {
                    logger.error("Error while recreating project")
                    return@showSnackBar
                }

                _stateFlow.update { state ->
                    state.copy(
                        projectCards = state.projectCards.toMutableList().apply {
                            add(projectPosition, mapProjectToProjectCard(recreatedProject))
                        }
                    )
                }
            }
        }

        _stateFlow.update { state ->
            state.copy(
                isLoading = false
            )
        }
    }

    override fun onProjectClick(id: UUID) = viewModelScope.launch {
        logger.debug("Project clicked: $id")

        navigationService.goTo(ProjectDetailsDestination(id))
    }

    override fun onAddButtonClick(): Job = viewModelScope.launch {
        navigationService.goTo(ProjectCreateDestination)
    }

    override fun onLoadSampleProjectsClick(): Job = viewModelScope.launch {
        logger.debug("Load sample project clicked")
        hideSnackBar()

        _stateFlow.update { state ->
            state.copy(
                isLoading = true
            )
        }

        val sampleProject = Project(
            id = UUID.random(),
            name = "ABC's",
            bpm = 110,
            rhythm = Rhythm.FOUR_FOURS,
            chords = listOf(
                Chord.Type.MAJOR.build(Note(60, 60)) to 4,
                Chord.Type.MINOR.build(Note(62, 62)) to 2,
                Chord.Type.DOMINANT_SEVENTH.build(Note(55, 55)) to 2,
                Chord.Type.MAJOR.build(Note(65, 65)) to 2,
                Chord.Type.MINOR.build(Note(64, 64)) to 2,
                Chord.Type.MINOR.build(Note(62, 62)) to 2,
                Chord.Type.MAJOR.build(Note(60, 60)) to 2
            ),
            melody = listOf(
                Note(60, 127) to 1,
                Note(60, 127) to 1,
                Note(67, 127) to 1,
                Note(67, 127) to 1,
                Note(69, 127) to 1,
                Note(69, 127) to 1,
                Note(67, 127) to 2,
                Note(65, 127) to 1,
                Note(65, 127) to 1,
                Note(64, 127) to 1,
                Note(64, 127) to 1,
                Note(62, 127) to 1,
                Note(62, 127) to 1,
                Note(60, 127) to 2
            )
        )

        saveProjectUseCase(CreateProjectErrorHandler(), sampleProject)
        val projects = getProjectsListUseCase()

        _stateFlow.update { state ->
            state.copy(
                isLoading = false,
                projectCards = projects.map(::mapProjectToProjectCard)
            )
        }
    }

    override fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?) {
        _stateFlow.update { state ->
            state.copy(
                snackBar = StateComponent.SnackBar(
                    message = message,
                    action = action,
                    onAction = onAction
                )
            )
        }
    }

    override fun hideSnackBar() {
        _stateFlow.update { state ->
            state.copy(
                snackBar = null
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        logger.debug("Cleared")
    }

//---

    internal fun mapProjectToProjectCard(project: Project): ProjectsListContract.State.ProjectCard = ProjectsListContract.State.ProjectCard(
        id = project.id,
        name = project.name,
        description = "BPM: ${project.bpm}\n${i18n.projectList.duration}: ${getProjectLengthInSeconds(project).round(2)}s",
        onDelete = ::onProjectDelete,
        onClick = ::onProjectClick
    )

    internal fun getProjectLengthInSeconds(project: Project): Double {
        return when(project.rhythm) {
            Rhythm.FOUR_FOURS  -> (project.lengthInMeasures * 60).toDouble() / project.bpm
            Rhythm.THREE_FOURS -> (project.lengthInMeasures * 60).toDouble() / project.bpm
        }
    }

//---

    inner class DeleteProjectErrorHandler : DeleteProjectUseCase.ErrorHandler {
        override fun handleDeleteProjectError() {
            logger.error("Error while deleting project")
        }
    }

//---

    inner class CreateProjectErrorHandler : SaveProjectUseCase.ErrorHandler {
        override fun onInvalidProjectName() {}

        override fun onInvalidProjectBpm() {}

        override fun onProjectSaveError() {
            hideSnackBar()
            showSnackBar(i18n.projectCreate.projectCreationError, i18n.common.retry) {
                onAddButtonClick()
            }
        }
    }
}