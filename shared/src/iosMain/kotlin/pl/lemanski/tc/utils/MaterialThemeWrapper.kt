package pl.lemanski.tc.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MaterialThemeWrapper(
    block: @Composable (PaddingValues) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val lightColorScheme = lightColorScheme(primary = Color(0xFF000000))
    val darkColorScheme = darkColorScheme(primary = Color(0xFFFFFFFF))

    val colorScheme = when {
        isDarkTheme -> darkColorScheme
        else        -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
    ) {
        Scaffold { paddingValues ->
            block(paddingValues)
        }
    }
}