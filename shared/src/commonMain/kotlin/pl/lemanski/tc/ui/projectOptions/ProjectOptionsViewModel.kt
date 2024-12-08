package pl.lemanski.tc.ui.projectOptions

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.navigation.ProjectOptionsDestination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.domain.useCase.getProject.GetProjectUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.exception.ViewModelInitException

internal class ProjectOptionsViewModel(
    override val key: ProjectOptionsDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val getProjectUseCase: GetProjectUseCase,
) : ProjectOptionsContract.ViewModel() {

    private val logger = Logger(this::class)
    private var project = getProjectUseCase(key.projectId) ?: throw ViewModelInitException("Project with id ${key.projectId} not found")
    private val initialState = ProjectOptionsContract.State(
        isLoading = false,
        projectName = "Alejandra Hale",
        backButton = StateComponent.Button(text = "ipsum", onClick = {}),
        snackBar = null
    )

    private val _stateFlow = MutableStateFlow(initialState)
    override val stateFlow: StateFlow<ProjectOptionsContract.State> = _stateFlow.asStateFlow()

    init {
        logger.debug("Init")
    }

    override fun onAttached() {
        logger.debug("Attached")

        _stateFlow.update { state ->
            state.copy(
                isLoading = false,
            )
        }
    }

    override fun back() {
        // FIXME run synchronously
        viewModelScope.launch {
            navigationService.back()
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
}

