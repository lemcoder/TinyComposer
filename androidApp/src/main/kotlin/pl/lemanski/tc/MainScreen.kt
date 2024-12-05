package pl.lemanski.tc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsRouter
import pl.lemanski.tc.ui.projectCreate.ProjectCreateRouter
import pl.lemanski.tc.ui.projectsList.ProjectListRouter
import pl.lemanski.tc.ui.welcome.WelcomeRouter

@Composable
fun MainScreen(
    paddingValues: PaddingValues,
    destination: Destination
) {
    Box(
        modifier = Modifier.padding(paddingValues)
    ) {
        when (destination) {
            WelcomeDestination       -> WelcomeRouter()
            ProjectListDestination   -> ProjectListRouter()
            ProjectCreateDestination -> ProjectCreateRouter()
            is ProjectDetailsDestination    -> ProjectDetailsRouter()
            is ProjectAiGenerateDestination -> {}
        }
    }
}