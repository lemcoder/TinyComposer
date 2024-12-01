package pl.lemanski.tc.ui.proejctDetails

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

internal class ProjectDetailsViewModel(
    override val key: ProjectDetailsDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val getProjectUseCase: GetProjectUseCase,
    private val playbackControlUseCase: PlaybackControlUseCase,
    private val generateAudioUseCase: GenerateAudioUseCase
) : ProjectDetailsContract.ViewModel() {

    private val logger = Logger(this::class)
    private var project = getProjectUseCase(key.projectId) ?: throw ViewModelInitException("Project with id ${key.projectId} not found")
    private val initialState = ProjectDetailsContract.State(
        isLoading = true,
        projectName = project.name,
        projectRhythm = project.rhythm.name(i18n),
        chordsTextArea = StateComponent.Input(
            value = "",
            type = StateComponent.Input.Type.TEXT,
            hint = "",
            error = null,
            onValueChange = ::onChordsTextAreaChanged
        ),
        playButton = StateComponent.Button(
            text = "",
            onClick = ::onPlayButtonClicked
        ),
        stopButton = null,
        aiGenerateButton = StateComponent.Button(text = "", onClick = ::onAiGenerateButtonClicked),
        snackBar = null,
        tempoInput = StateComponent.Input(
            value = project.bpm.toString(),
            type = StateComponent.Input.Type.NUMBER,
            hint = i18n.projectDetails.tempo,
            error = null,
            onValueChange = ::onTempoInputChanged
        ),
        backButton = StateComponent.Button(text = "", onClick = ::back),
    )

    private val _stateFlow = MutableStateFlow(initialState)
    override val stateFlow: StateFlow<ProjectDetailsContract.State> = _stateFlow.asStateFlow()

    init {
        logger.debug("Init")
    }

    override fun onAttached() {
        logger.debug("Attached")

        _stateFlow.update { state ->
            state.copy(isLoading = false)
        }
    }

    override fun onPlayButtonClicked(): Job = viewModelScope.launch {
        val tempo = _stateFlow.value.tempoInput.value.toIntOrNull() ?: run {
            _stateFlow.update { state ->
                state.copy(
                    tempoInput = state.tempoInput.copy(
                        error = i18n.projectDetails.invalidTempo
                    )
                )
            }
            return@launch
        }

        _stateFlow.update {
            it.copy(
                playButton = null,
                stopButton = StateComponent.Button(text = "", onClick = ::onStopButtonClicked)
            )
        }

        val audioData = generateAudioUseCase(GenerateAudioErrorHandler(), project.chords, tempo)
        playbackControlUseCase.play(PlaybackControlErrorHandler(), audioData)
    }

    override fun onStopButtonClicked(): Job = viewModelScope.launch {
        playbackControlUseCase.stop(PlaybackControlErrorHandler())

        _stateFlow.update {
            it.copy(
                playButton = StateComponent.Button(text = "", onClick = ::onPlayButtonClicked),
                stopButton = null
            )
        }
    }

    override fun onAiGenerateButtonClicked() {
        logger.debug("AI generate button clicked")
    }

    override fun onChordsTextAreaChanged(text: String) {
        _stateFlow.update { state ->
            state.copy(
                chordsTextArea = state.chordsTextArea.copy(
                    value = text,
                    error = null
                )
            )
        }

        runCatching {
            val chords = text.tryDecodeChordBeats()
            project = project.copy(chords = chords)
        }
    }

    override fun onTempoInputChanged(tempo: String) {
        tempo.toIntOrNull() ?: return

        _stateFlow.update { state ->
            state.copy(
                tempoInput = state.tempoInput.copy(
                    value = tempo,
                    error = null
                )
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

    //---

    inner class PlaybackControlErrorHandler : PlaybackControlUseCase.ErrorHandler {
        override fun onAudioDataNotInitialized() {
        }

        override fun onControlStateError() {
            logger.error("Control state error")
            hideSnackBar()
            showSnackBar(i18n.projectDetails.controlStateError, null, null)
        }
    }

    //---

    inner class GenerateAudioErrorHandler : GenerateAudioUseCase.ErrorHandler {
        override fun onInvalidChordBeats() {
            _stateFlow.update { state ->
                state.copy(
                    chordsTextArea = state.chordsTextArea.copy(
                        error = i18n.projectDetails.invalidChordBeats
                    )
                )
            }
        }
    }
}

