package pl.lemanski.tc.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun TcTheme(content: @Composable () -> Unit) {
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
        content()
    }
}