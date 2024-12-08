package pl.lemanski.tc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.ProjectAiGenerateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectCreateDestination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.domain.model.navigation.ProjectOptionsDestination
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.theme.TcTheme
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsRouter
import pl.lemanski.tc.ui.projectAiGenerate.ProjectAiGenerateRouter
import pl.lemanski.tc.ui.projectCreate.ProjectCreateRouter
import pl.lemanski.tc.ui.projectOptions.ProjectOptionsRouter
import pl.lemanski.tc.ui.projectsList.ProjectListRouter
import pl.lemanski.tc.ui.welcome.WelcomeRouter

class MainActivity : ComponentActivity() {
    private val navigationService: NavigationService by inject()
    private val navigationState = mutableStateOf<Destination>(WelcomeDestination)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        navigationService.setOnNavigateListener { event ->
            navigationState.value = event.destination
        }

        setContent {
            val scope = rememberCoroutineScope()
            val destination by navigationState

            TcTheme {
                Surface {
                    when (destination) {
                        WelcomeDestination              -> WelcomeRouter()
                        ProjectListDestination          -> ProjectListRouter()
                        ProjectCreateDestination        -> ProjectCreateRouter()
                        is ProjectDetailsDestination    -> ProjectDetailsRouter()
                        is ProjectAiGenerateDestination -> ProjectAiGenerateRouter()
                        is ProjectOptionsDestination    -> ProjectOptionsRouter()
                    }
                }
            }

            BackHandler {
                scope.launch {
                    navigationService.back()
                }
            }
        }
    }
}