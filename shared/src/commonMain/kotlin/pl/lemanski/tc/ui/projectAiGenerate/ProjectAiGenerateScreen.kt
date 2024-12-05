package pl.lemanski.tc.ui.projectAiGenerate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.ToComposable
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.ToComposable

@Composable
internal fun ProjectAiGenerateScreen(
    isLoading: Boolean,
    text: String,
    promptInput: StateComponent.Input,
    submitButton: StateComponent.Button,
    backButton: StateComponent.Button,
    snackBar: StateComponent.SnackBar?,
) {
    LoaderScaffold(isLoading) { snackbarHostState ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row {
                promptInput.ToComposable()
                submitButton.ToComposable()
            }

            Text(
                text = text,
            )

            snackBar?.ToComposable(snackbarHostState)
        }
    }
}