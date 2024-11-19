package pl.lemanski.tc

import androidx.compose.runtime.Composable
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectsDestination
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.ui.projectCreate.ProjectCreateRouter
import pl.lemanski.tc.ui.projectsList.ProjectListRouter
import pl.lemanski.tc.ui.welcome.WelcomeRouter

@Composable
fun MainScreen(
    destination: Destination
) {
    when (destination) {
        WelcomeDestination  -> WelcomeRouter()
        ProjectsDestination -> ProjectListRouter()
        ProjectCreateDestination     -> ProjectCreateRouter()
        is ProjectDetailsDestination -> {}
    }
}