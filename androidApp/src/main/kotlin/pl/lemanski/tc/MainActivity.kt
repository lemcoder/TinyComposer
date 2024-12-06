package pl.lemanski.tc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.theme.TcTheme

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
                // TODO replace with window insets
                Scaffold { paddingValues ->
                    MainScreen(paddingValues, destination)
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