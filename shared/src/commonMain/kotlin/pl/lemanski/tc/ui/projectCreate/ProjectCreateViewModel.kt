package pl.lemanski.tc.ui.projectCreate

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.domain.service.navigation.key
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.exception.NavigationStateException

internal class ProjectCreateViewModel(
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val projectRepository: ProjectRepository,
) : ProjectCreateContract.ViewModel() {
    private val logger = Logger(this::class)

    private val _stateFlow = MutableStateFlow(
        ProjectCreateContract.State(
            isLoading = true,
            title = i18n.projectCreate.title,
            projectName = StateComponent.Input(
                value = "",
                type = StateComponent.Input.Type.TEXT,
                hint = i18n.projectCreate.projectName,
                onValueChange = ::onProjectNameInputChange
            ),
            projectBpm = StateComponent.Input(
                value = "",
                type = StateComponent.Input.Type.NUMBER,
                hint = i18n.projectCreate.projectBpm,
                onValueChange = ::onProjectBpmInputChange
            ),
            projectRhythm = StateComponent.SelectInput(
                value = Rhythm.FOUR_FOURS,
                onSelected = ::onProjectRhythmSelectChange,
                hint = i18n.projectCreate.projectRhythm,
                options = Rhythm.entries.map(::rhythmToInputOption).toSet()
            ),
            createProjectButton = StateComponent.Button(
                text = i18n.projectCreate.createProjectButton,
                onClick = ::onCreateProjectClick
            )

        )
    )

    override val key: ProjectCreateDestination = navigationService.key<ProjectCreateDestination>() ?: throw NavigationStateException("Key not found")
    override val stateFlow: StateFlow<ProjectCreateContract.State> = _stateFlow.asStateFlow()


    override fun initialize() {
        logger.debug("Initialize")

        _stateFlow.update { state ->
            state.copy(isLoading = false)
        }
    }

    override fun onProjectNameInputChange(value: String) {
        logger.debug("onProjectNameInputChange: $value")

        _stateFlow.update { state ->
            state.copy(
                projectName = state.projectName.copy(value = value)
            )
        }
    }

    override fun onProjectBpmInputChange(value: String) {
        logger.debug("onProjectBpmInputChange: $value")

        _stateFlow.update { state ->
            state.copy(
                projectBpm = state.projectBpm.copy(value = value)
            )
        }
    }

    override fun onProjectRhythmSelectChange(value: Rhythm) {
        logger.debug("onProjectRhythmSelectChange: $value")

        _stateFlow.update { state ->
            state.copy(
                projectRhythm = state.projectRhythm.copy(value = value)
            )
        }
    }

    override fun onCreateProjectClick() {
        logger.debug("onCreateProjectClick")

        val projectName = _stateFlow.value.projectName.value
        val projectBpm = _stateFlow.value.projectBpm.value.toIntOrNull()
        val projectRhythm = _stateFlow.value.projectRhythm.value

        if (projectName.isBlank() || projectBpm == null) {
            logger.error("Project name or bpm is empty")
            // TODO add error handling on UI
            return
        }

        viewModelScope.launch {
            logger.debug("Saving project")

            val project = Project(
                id = UUID.random(),
                name = projectName,
                lengthInMeasures = 0,
                bpm = projectBpm,
                rhythm = projectRhythm,
                chords = listOf()
            )

            projectRepository.saveProject(project)

            navigationService.back()
        }
    }

    //

    private fun rhythmToInputOption(rhythm: Rhythm) = StateComponent.SelectInput.Option(
        name = rhythm.name,
        value = rhythm
    )
}

