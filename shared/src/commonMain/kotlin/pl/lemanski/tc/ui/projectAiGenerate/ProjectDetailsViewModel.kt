package pl.lemanski.tc.ui.projectAiGenerate

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.data.persistent.decoder.tryDecodeChordBeats
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.project.name
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.domain.useCase.generateAudioUseCase.GenerateAudioUseCase
import pl.lemanski.tc.domain.useCase.getProject.GetProjectUseCase
import pl.lemanski.tc.domain.useCase.playbackControlUseCase.PlaybackControlUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.exception.ViewModelInitException

internal class ProjectAiGenerateViewModel(
    override val key: ProjectDetailsDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val getProjectUseCase: GetProjectUseCase,
) : ProjectAiGenerateContract.ViewModel() {
    private val logger = Logger(this::class)
    private var project = getProjectUseCase(key.projectId) ?: throw ViewModelInitException("Project with id ${key.projectId} not found")
    private val initialState = ProjectAiGenerateContract.State(
        isLoading = true,
        snackBar = null,
        backButton = StateComponent.Button(
            text = "",
            onClick = ::back
        ),
        text = "",
        promptInput = StateComponent.Input(
            value = "",
            type = StateComponent.Input.Type.TEXT,
            hint = "Type prompt here",
            error = null,
            onValueChange = {}

        ),
        submitButton = StateComponent.Button(
            text = "Submit",
            onClick = {}

        ),
    )

    private val _stateFlow = MutableStateFlow(initialState)
    override val stateFlow: StateFlow<ProjectAiGenerateContract.State> = _stateFlow.asStateFlow()

    init {
        logger.debug("Init")
    }

    override fun onAttached() {
        logger.debug("Attached")

        _stateFlow.update { state ->
            state.copy(isLoading = false)
        }
    }

    override fun back() {
        // FIXME run synchronously
        viewModelScope.launch {
            navigationService.back()
        }
    }

    override fun onSubmitClicked() {
        TODO("Not yet implemented")
    }

    override fun onPromptInputChanged(input: String) {
        TODO("Not yet implemented")
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
}

