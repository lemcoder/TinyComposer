package pl.lemanski.tc.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TcTheme(content: @Composable () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val lightColorScheme = lightColorScheme(
        primary = Color(0xFF212432),
    )
    val darkColorScheme = darkColorScheme(
        primary = Color(0xFFD9DEED),
    )

    val colorScheme = when {
        isDarkTheme -> darkColorScheme
        else        -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
    ) {
        content()
    }
}