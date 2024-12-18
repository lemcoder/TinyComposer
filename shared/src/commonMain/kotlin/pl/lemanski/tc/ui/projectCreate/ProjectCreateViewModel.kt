package pl.lemanski.tc.ui.projectCreate

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.domain.model.project.name
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.domain.useCase.saveProject.SaveProjectUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

internal class ProjectCreateViewModel(
    override val key: ProjectCreateDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val saveProjectUseCase: SaveProjectUseCase
) : ProjectCreateContract.ViewModel() {
    private val logger = Logger(this::class)
    private val initialState = ProjectCreateContract.State(
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
            selected = rhythmToInputOption(Rhythm.FOUR_FOURS),
            onSelected = ::onProjectRhythmSelectChange,
            label = i18n.projectCreate.projectRhythm,
            options = Rhythm.entries.map(::rhythmToInputOption).toSet()
        ),
        createProjectButton = StateComponent.Button(
            text = i18n.projectCreate.createProjectButton,
            onClick = ::onCreateProjectClick
        ),
        errorSnackBar = null
    )

    private val _stateFlow = MutableStateFlow(initialState)
    override val stateFlow: StateFlow<ProjectCreateContract.State> = _stateFlow.asStateFlow()

    init {
        logger.debug("Initialize")
    }

    override fun onAttached() {
        logger.debug("Attached")

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

    override fun onProjectRhythmSelectChange(selected: StateComponent.SelectInput.Option<Rhythm>) {
        logger.debug("onProjectRhythmSelectChange: $selected")

        _stateFlow.update { state ->
            state.copy(
                projectRhythm = state.projectRhythm.copy(selected = selected)
            )
        }
    }

    override fun onCreateProjectClick() {
        logger.debug("onCreateProjectClick")

        clearErrors()

        val projectName = _stateFlow.value.projectName.value
        val projectBpm = _stateFlow.value.projectBpm.value.toIntOrNull() ?: 0
        val projectRhythm = _stateFlow.value.projectRhythm.selected.value

        saveProjectUseCase(
            errorHandler = CreateProjectErrorHandler(),
            project = Project(
                id = UUID.random(),
                name = projectName,
                bpm = projectBpm,
                rhythm = projectRhythm,
                chords = listOf(),
                melody = listOf()
            )
        ) ?: return

        navigationService.back()
    }

    override fun clearErrors() {
        _stateFlow.update { state ->
            state.copy(
                projectName = state.projectName.copy(
                    error = null
                ),
                projectBpm = state.projectBpm.copy(
                    error = null
                ),
            )
        }
    }

    override fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?) {
        _stateFlow.update { state ->
            state.copy(
                errorSnackBar = StateComponent.SnackBar(
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
                errorSnackBar = null
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        logger.debug("Cleared")
    }

    //

    private fun rhythmToInputOption(rhythm: Rhythm) = StateComponent.SelectInput.Option(
        name = rhythm.name(i18n),
        value = rhythm
    )

    //---

    inner class CreateProjectErrorHandler : SaveProjectUseCase.ErrorHandler {
        override fun onInvalidProjectName() {
            _stateFlow.update { state ->
                state.copy(
                    projectName = state.projectName.copy(
                        error = i18n.projectCreate.invalidProjectName
                    )
                )
            }
        }

        override fun onInvalidProjectBpm() {
            _stateFlow.update { state ->
                state.copy(
                    projectBpm = state.projectBpm.copy(
                        error = i18n.projectCreate.invalidProjectBpm
                    )
                )
            }
        }

        override fun onProjectSaveError() {
            hideSnackBar()
            showSnackBar(i18n.projectCreate.projectCreationError, i18n.common.retry) {
                onCreateProjectClick()
            }
        }
    }
}

