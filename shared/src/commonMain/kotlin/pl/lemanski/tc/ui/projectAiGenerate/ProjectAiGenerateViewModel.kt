package pl.lemanski.tc.ui.projectAiGenerate

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.domain.useCase.aiGenerate.AiGenerateUseCase
import pl.lemanski.tc.domain.useCase.loadProject.LoadProjectUseCase
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.exception.ViewModelInitException

internal class ProjectAiGenerateViewModel(
    override val key: ProjectAiGenerateDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val loadProjectUseCase: LoadProjectUseCase,
    private val aiGenerateUseCase: AiGenerateUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase
) : ProjectAiGenerateContract.ViewModel() {

    private val promptOptions = ProjectAiGenerateContract.PromptOption.entries.map { it.toOption() }
    private val logger = Logger(this::class)
    private var project = loadProjectUseCase(key.projectId) ?: throw ViewModelInitException("Project with id ${key.projectId} not found")
    private val initialState = ProjectAiGenerateContract.State(
        isLoading = true,
        snackBar = null,
        title = project.name,
        promptInput = StateComponent.Input(
            value = "",
            type = StateComponent.Input.Type.TEXT,
            hint = i18n.projectAiGenerate.promptHint,
            error = null,
            onValueChange = ::onPromptInputChanged
        ),
        promptOptions = StateComponent.RadioGroup(
            selected = promptOptions.first(),
            label = i18n.projectAiGenerate.promptOptions,
            onSelected = ::onPromptOptionSelected,
            options = promptOptions.toSet()
        ),
        submitButton = StateComponent.Button(
            text = i18n.common.confirm,
            onClick = ::onSubmitClicked
        )
    )

    private val _stateFlow: MutableStateFlow<ProjectAiGenerateContract.State> = MutableStateFlow(initialState)
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
        navigationService.back()
    }

    override fun onSubmitClicked() = viewModelScope.launch {
        _stateFlow.update { state ->
            state.copy(
                isLoading = true
            )
        }

        val selectedPromptOption = _stateFlow.value.promptOptions.selected.value
        val prompt = _stateFlow.value.promptInput.value
        val errorHandler = AiGenerateErrorHandler()

        val chordBeats = when (selectedPromptOption) {
            ProjectAiGenerateContract.PromptOption.CHORDS_FOR_MELODY -> aiGenerateUseCase.generateChordBeats(errorHandler, "TODO")
            ProjectAiGenerateContract.PromptOption.CHORDS            -> aiGenerateUseCase.generateChordBeats(errorHandler, prompt)
            else                                                     -> project.chords
        }

        val noteBeats = when (selectedPromptOption) {
            ProjectAiGenerateContract.PromptOption.MELODY_FOR_CHORDS -> aiGenerateUseCase.generateMelody(errorHandler, "TODO")
            ProjectAiGenerateContract.PromptOption.MELODY            -> aiGenerateUseCase.generateMelody(errorHandler, prompt)
            else                                                     -> project.melody
        }

        project = project.copy(
            chords = chordBeats,
            melody = noteBeats
        )

        updateProjectUseCase.invoke(
            errorHandler = UpdateProjectErrorHandler(),
            project = project,
            projectId = project.id
        )

        _stateFlow.update { state ->
            state.copy(
                isLoading = false
            )
        }
    }

    override fun onPromptInputChanged(input: String) {
        _stateFlow.update { state ->
            state.copy(
                promptInput = state.promptInput.copy(
                    value = input
                )
            )
        }
    }

    override fun onPromptOptionSelected(option: StateComponent.RadioGroup.Option<ProjectAiGenerateContract.PromptOption>) {
        _stateFlow.update { state ->
            state.copy(
                promptOptions = state.promptOptions.copy(
                    selected = option
                )
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

    private fun ProjectAiGenerateContract.PromptOption.toOption(): StateComponent.RadioGroup.Option<ProjectAiGenerateContract.PromptOption> {
        return StateComponent.RadioGroup.Option(
            name = name(),
            value = this
        )
    }

    private fun ProjectAiGenerateContract.PromptOption.name(): String {
        return when (this) {
            ProjectAiGenerateContract.PromptOption.CHORDS_FOR_MELODY -> i18n.projectAiGenerate.promptOptionChordsForMelody
            ProjectAiGenerateContract.PromptOption.MELODY_FOR_CHORDS -> i18n.projectAiGenerate.promptOptionMelodyForChords
            ProjectAiGenerateContract.PromptOption.CHORDS            -> i18n.projectAiGenerate.promptOptionChords
            ProjectAiGenerateContract.PromptOption.MELODY            -> i18n.projectAiGenerate.promptOptionMelody
        }
    }

    //---

    inner class AiGenerateErrorHandler : AiGenerateUseCase.ErrorHandler {
        override fun onParsingError() {
            showSnackBar(i18n.projectAiGenerate.parsingError, null, null)
        }

        override fun onNetworkError() {
            showSnackBar(i18n.projectAiGenerate.networkError, null, null)
        }

        override fun onUnknownError() {
            showSnackBar(i18n.projectAiGenerate.unknownError, null, null)
        }
    }

    //---

    inner class UpdateProjectErrorHandler : UpdateProjectUseCase.ErrorHandler {
        override fun onInvalidProjectName() {
            // will not happen
        }

        override fun onInvalidProjectBpm() {

        }
    }
}

