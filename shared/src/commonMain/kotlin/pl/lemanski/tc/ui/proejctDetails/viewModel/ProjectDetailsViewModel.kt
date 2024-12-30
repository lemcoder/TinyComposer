package pl.lemanski.tc.ui.proejctDetails.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.audio.AudioStream
import pl.lemanski.tc.domain.model.audio.play
import pl.lemanski.tc.domain.model.audio.stop
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectOptionsDestination
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.domain.service.navigation.goTo
import pl.lemanski.tc.domain.useCase.generateAudio.GenerateAudioUseCase
import pl.lemanski.tc.domain.useCase.loadProject.LoadProjectUseCase
import pl.lemanski.tc.domain.useCase.projectPresetsControl.PresetsControlUseCase
import pl.lemanski.tc.domain.useCase.saveProject.SaveProjectUseCase
import pl.lemanski.tc.domain.useCase.setMarkersUseCase.SetMarkersUseCase
import pl.lemanski.tc.domain.useCase.updateProject.UpdateProjectUseCase
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.exception.ViewModelInitException

internal class ProjectDetailsViewModel(
    override val key: ProjectDetailsDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService,
    private val loadProjectUseCase: LoadProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    private val generateAudioUseCase: GenerateAudioUseCase,
    private val presetsControlUseCase: PresetsControlUseCase,
    private val saveProjectUseCase: SaveProjectUseCase,
    private val setMarkersUseCase: SetMarkersUseCase
) : ProjectDetailsContract.ViewModel() {

    private var audioStream: AudioStream = AudioStream.EMPTY

    private val logger = Logger(this::class)
    private val chordPageViewModel = ChordPageViewModel(
        projectId = key.projectId,
        viewModelScope = viewModelScope,
        updateProjectUseCase = updateProjectUseCase,
        loadProjectUseCase = loadProjectUseCase,
        projectDetailsViewModel = this,
        i18n = i18n
    )

    private val melodyPageViewModel = MelodyPageViewModel(
        projectId = key.projectId,
        viewModelScope = viewModelScope,
        updateProjectUseCase = updateProjectUseCase,
        loadProjectUseCase = loadProjectUseCase,
        projectDetailsViewModel = this,
        i18n = i18n
    )

    private val currentViewModel: ProjectDetailsContract.PageViewModel
        get() = when (mutableStateFlow.value.tabComponent.selected.value) {
            ProjectDetailsContract.Tab.CHORDS -> chordPageViewModel
            ProjectDetailsContract.Tab.MELODY -> melodyPageViewModel
        }

    private val initialProjectValue: Project = loadProjectOrThrow(key.projectId)
    private val initialState = ProjectDetailsContract.State(
        isLoading = true,
        projectName = initialProjectValue.name,
        playButton = StateComponent.Button(
            text = "",
            onClick = ::onPlayButtonClicked
        ),
        stopButton = null,
        backButton = StateComponent.Button(
            text = "",
            onClick = ::back
        ),
        aiGenerateButton = StateComponent.Button(
            text = "",
            onClick = ::onAiGenerateButtonClicked
        ),
        snackBar = null,
        tabComponent = StateComponent.TabComponent(
            selected = tabToOption(ProjectDetailsContract.Tab.CHORDS),
            options = ProjectDetailsContract.Tab.entries.map { tabToOption(it) }.toSet(),
            onTabSelected = ::onTabSelected
        ),
        pageState = ProjectDetailsContract.State.PageState.EMPTY,
        projectDetailsButton = StateComponent.Button(
            text = "",
            onClick = ::onProjectOptionsButtonClicked
        ),
    )

    override val mutableStateFlow = MutableStateFlow(initialState)
    override val stateFlow: StateFlow<ProjectDetailsContract.State> = mutableStateFlow.asStateFlow()

    init {
        logger.debug("Init")
    }

    override fun onAttached() {
        logger.debug("Attached")

        mutableStateFlow.update { state ->
            state.copy(
                isLoading = false,
            )
        }
    }

    override fun onPlayButtonClicked(): Job = viewModelScope.launch {
        val project = loadProjectOrThrow(key.projectId)

        mutableStateFlow.update {
            it.copy(
                playButton = null,
                stopButton = StateComponent.Button(
                    text = "",
                    onClick = ::onStopButtonClicked
                )
            )
        }

        val isLoopingEnabled = true // TODO implement looping control
        val chordPreset = presetsControlUseCase.getChordPreset(project.id)
        val notePreset = presetsControlUseCase.getMelodyPreset(project.id)

        audioStream = generateAudioUseCase(
            errorHandler = GenerateAudioErrorHandler(),
            chordBeats = project.chords,
            chordsPreset = chordPreset,
            noteBeats = project.melody,
            notesPreset = notePreset,
            tempo = project.bpm
        )

        audioStream.onMarkerReached { marker ->
            logger.error("Marker reached: ${marker.index}")
            if (marker == AudioStream.END_MARKER) {
                mutableStateFlow.update {
                    it.copy(
                        playButton = StateComponent.Button(
                            text = "",
                            onClick = ::onPlayButtonClicked
                        ),
                        stopButton = null
                    )
                }
                audioStream.stop()
                return@onMarkerReached
            }

            val markerIndex = marker.index

            mutableStateFlow.update { state ->
                state.copy(
                    pageState = state.pageState.copy(
                        chordBeats = state.pageState.chordBeats.map {
                            it.copy(isActive = it.id == markerIndex)
                        },
                        noteBeats = state.pageState.noteBeats.map {
                            it.copy(isActive = it.id == markerIndex)
                        }
                    )
                )
            }
        }

        setMarkersUseCase(
            project = project,
            audioStream = audioStream
        )

        audioStream.play(isLoopingEnabled)
    }

    override fun onStopButtonClicked(): Job = viewModelScope.launch {
        audioStream.stop()

        mutableStateFlow.update {
            it.copy(
                playButton = StateComponent.Button(text = "", onClick = ::onPlayButtonClicked),
                stopButton = null
            )
        }
    }

    override fun onAiGenerateButtonClicked() {
        navigationService.goTo(ProjectAiGenerateDestination(key.projectId))
    }

    override fun onTabSelected(tab: ProjectDetailsContract.Tab) {
        mutableStateFlow.update { state ->
            state.copy(
                tabComponent = state.tabComponent.copy(
                    selected = tabToOption(tab),
                )
            )
        }

        currentViewModel.onAttached()
    }

    //---

    override fun back() {
        navigationService.back()
    }

    override fun showSnackBar(message: String, action: String?, onAction: (() -> Unit)?) {
        mutableStateFlow.update { state ->
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
        mutableStateFlow.update { state ->
            state.copy(
                snackBar = null
            )
        }
    }

    override fun onProjectOptionsButtonClicked() {
        navigationService.goTo(ProjectOptionsDestination(key.projectId))
    }

    override fun onCleared() {
        super.onCleared()

        val project = loadProjectOrThrow(key.projectId)
        saveProjectUseCase(SaveProjectUseCaseErrorHandler(), project)

        logger.debug("Cleared")
    }

    //---

    private fun loadProjectOrThrow(projectId: UUID): Project {
        return loadProjectUseCase(projectId) ?: throw ViewModelInitException("Project with id $projectId not found")
    }

    private fun tabToOption(tab: ProjectDetailsContract.Tab): StateComponent.TabComponent.Tab<ProjectDetailsContract.Tab> = StateComponent.TabComponent.Tab(
        name = when (tab) {
            ProjectDetailsContract.Tab.CHORDS -> i18n.projectDetails.chordsTab
            ProjectDetailsContract.Tab.MELODY -> i18n.projectDetails.melodyTab
        },
        value = tab
    )

    //---

    inner class GenerateAudioErrorHandler : GenerateAudioUseCase.ErrorHandler {
        override fun onInvalidChordBeats() {
            mutableStateFlow.update { state ->
                state.copy(
                    snackBar = StateComponent.SnackBar(
                        message = i18n.projectDetails.invalidChordBeats,
                        action = null,
                        onAction = null
                    )
                )
            }
        }

        override fun onInvalidNoteBeats() {
            mutableStateFlow.update { state ->
                state.copy(
                    snackBar = StateComponent.SnackBar(
                        message = i18n.projectDetails.invalidNoteBeats,
                        action = null,
                        onAction = null
                    )
                )
            }
        }
    }

    //---

    inner class SaveProjectUseCaseErrorHandler : SaveProjectUseCase.ErrorHandler {
        override fun onInvalidProjectName() {
            // Will not happen
        }

        override fun onInvalidProjectBpm() {
            // Will not happen
        }

        override fun onProjectSaveError() {
            showSnackBar(i18n.projectDetails.projectSaveError, null, null)
        }
    }
}

