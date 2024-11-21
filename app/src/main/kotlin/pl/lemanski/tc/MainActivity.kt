package pl.lemanski.tc

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        enableEdgeToEdge()

        App.koinInstance.androidContext(this@MainActivity)
        navigationService.setOnNavigateListener(OnNavigateListenerImpl())

        setContent {
            val destination by navigationState

            val isDarkTheme = isSystemInDarkTheme()
            val supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            val lightColorScheme = lightColorScheme(primary = Color(0xFF1EB980))
            val darkColorScheme = darkColorScheme(primary = Color(0xFF66ffc7))
            val colorScheme = when {
                supportsDynamicColor && isDarkTheme  -> dynamicDarkColorScheme(LocalContext.current)
                supportsDynamicColor && !isDarkTheme -> dynamicLightColorScheme(LocalContext.current)
                isDarkTheme                          -> darkColorScheme
                else                                 -> lightColorScheme
            }
            val typography = Typography(
                displaySmall = TextStyle(fontWeight = FontWeight.W100, fontSize = 96.sp),
                labelLarge = TextStyle(fontWeight = FontWeight.W600, fontSize = 14.sp)
            )
            val shapes = Shapes(
                extraSmall = RoundedCornerShape(3.0.dp),
                small = RoundedCornerShape(6.0.dp)
            )

            MaterialTheme(
                colorScheme = colorScheme,
                typography = typography,
                shapes = shapes
            ) {
                Scaffold { paddingValues ->
                    MainScreen(paddingValues, destination)
                }
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