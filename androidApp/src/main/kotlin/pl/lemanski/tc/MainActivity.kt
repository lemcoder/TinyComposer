package pl.lemanski.tc

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back

class MainActivity : ComponentActivity() {
    private val navigationService: NavigationService by inject()
    private val navigationState = mutableStateOf<Destination>(WelcomeDestination)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        TCApp.koinInstance.androidContext(this@MainActivity)
        navigationService.setOnNavigateListener { event ->
            navigationState.value = event.destination
        }

        setContent {
            val scope = rememberCoroutineScope()
            val destination by navigationState

            val isDarkTheme = isSystemInDarkTheme()
            val supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            val lightColorScheme = lightColorScheme(primary = Color(0xFF000000))
            val darkColorScheme = darkColorScheme(primary = Color(0xFFFFFFFF))

            val colorScheme = when {
                supportsDynamicColor && isDarkTheme  -> dynamicDarkColorScheme(LocalContext.current)
                supportsDynamicColor && !isDarkTheme -> dynamicLightColorScheme(LocalContext.current)
                isDarkTheme                          -> darkColorScheme
                else                                 -> lightColorScheme
            }

            MaterialTheme(
                colorScheme = colorScheme,
            ) {
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