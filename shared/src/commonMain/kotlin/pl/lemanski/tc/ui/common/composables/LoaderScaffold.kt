package pl.lemanski.tc.ui.common.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import tinycomposer.shared.generated.resources.Res

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun LoaderScaffold(
    isLoading: Boolean,
    content: @Composable (SnackbarHostState) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarState) },
    ) { paddingValues ->
        LoadingScrim(isVisible = isLoading)

        content(snackbarState)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoadingScrim(isVisible: Boolean) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/anim/notes.json").decodeToString()
        )
    }

    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent background
                .pointerInput(Unit) {} // Consume all pointer events
                .zIndex(100f)
        ) {
            Icon(
                painter = rememberLottiePainter(
                    composition = composition,
                    iterations = Compottie.IterateForever
                ),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Note loading animation",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}