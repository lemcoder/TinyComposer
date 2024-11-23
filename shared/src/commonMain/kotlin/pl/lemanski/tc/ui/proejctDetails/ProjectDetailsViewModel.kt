package pl.lemanski.tc.ui.proejctDetails

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
import pl.lemanski.tc.domain.service.navigation.key
import pl.lemanski.tc.domain.useCase.createProject.CreateProjectUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.exception.NavigationStateException

internal class ProjectDetailsViewModel(
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val createProjectUseCase: CreateProjectUseCase
) : ProjectDetailsContract.ViewModel() {
    private val logger = Logger(this::class)

    private val _stateFlow = MutableStateFlow(
        ProjectDetailsContract.State(
            isLoading = true,
            title = i18n.projectCreate.title,
            projectName = "Sterling Frye",
            projectBpm = "sapientem",
            projectRhythm = "utroque",
            chordsTextArea = StateComponent.Input(value = "tellus", type = StateComponent.Input.Type.TEXT, hint = "vero", error = null, onValueChange = {}),
            playButton = null,
            stopButton = null,
            aiGenerateButton = StateComponent.Button(text = "et", onClick = {}),
            errorSnackBar = null,
        )
    )

    override val key: ProjectCreateDestination = navigationService.key<ProjectCreateDestination>() ?: throw NavigationStateException("Key not found")
    override val stateFlow: StateFlow<ProjectDetailsContract.State> = _stateFlow.asStateFlow()


    override fun initialize() {
        logger.debug("Initialize")

        _stateFlow.update { state ->
            state.copy(isLoading = false)
        }
    }


}

