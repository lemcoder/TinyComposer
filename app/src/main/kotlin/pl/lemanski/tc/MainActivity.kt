package pl.lemanski.tc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.NavigationEvent
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.OnNavigateListener
import pl.lemanski.tc.domain.service.navigation.back

class MainActivity : ComponentActivity() {
    private val navigationService: NavigationService by inject()
    private val navigationState = mutableStateOf<Destination>(WelcomeDestination)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.koinInstance.androidContext(this@MainActivity)
        navigationService.setOnNavigateListener(OnNavigateListenerImpl())

        setContent {
            val destination by navigationState

            MaterialTheme {
                MainScreen(destination)
            }

            BackHandler {
                navigationService.back()
            }
        }
    }

    inner class OnNavigateListenerImpl : OnNavigateListener {
        override fun onNavigate(event: NavigationEvent) {
            navigationState.value = event.destination
        }
    }
}