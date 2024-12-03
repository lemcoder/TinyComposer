package pl.lemanski.tc.ui.welcome

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.goTo
import pl.lemanski.tc.domain.service.navigation.replaceAll
import pl.lemanski.tc.ui.common.i18n.I18n
import pl.lemanski.tc.utils.Logger

internal class WelcomeViewModel(
    override val key: WelcomeDestination,
    private val i18n: I18n,
    private val navigationService: NavigationService
) : WelcomeContract.ViewModel() {

    private val logger = Logger(this::class)
    private val initialState = WelcomeContract.State(
        title = i18n.welcome.title,
    )

    private val _stateFlow = MutableStateFlow(initialState)
    override val stateFlow: StateFlow<WelcomeContract.State> = _stateFlow.asStateFlow()

    override fun onAttached() {
        logger.debug("Initialize")
        goToProjectsList()
    }

    override fun goToProjectsList(): Job = viewModelScope.launch {
        logger.debug("Go to projects list")
        navigationService.replaceAll(ProjectListDestination)
    }
}