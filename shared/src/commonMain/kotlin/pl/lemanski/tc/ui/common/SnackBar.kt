package pl.lemanski.tc.ui.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
internal fun StateComponent.SnackBar?.ToComposable(
    snackBarState: SnackbarHostState,
) {
    LaunchedEffect(this) {
        if (this@ToComposable == null) {
            snackBarState.currentSnackbarData?.dismiss()
            return@LaunchedEffect
        }

        val result = snackBarState.showSnackbar(this@ToComposable.message, this@ToComposable.action, true)
        when (result) {
            SnackbarResult.Dismissed       -> { /* do nothing */ }
            SnackbarResult.ActionPerformed -> this@ToComposable.onAction?.invoke()
        }
    }
}