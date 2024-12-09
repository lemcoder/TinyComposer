package pl.lemanski.tc.ui.projectAiGenerate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.ToComposable
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.ToComposable
import pl.lemanski.tc.ui.projectAiGenerate.ProjectAiGenerateContract.PromptOption

@Composable
internal fun ProjectAiGenerateScreen(
    isLoading: Boolean,
    title: String,
    promptOptions: StateComponent.RadioGroup<PromptOption>,
    promptInput: StateComponent.Input,
    submitButton: StateComponent.Button,
    text: String,
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
            promptOptions.ToComposable()

            promptInput.ToComposable()

            submitButton.ToComposable()

            Text(text = text)

            snackBar?.ToComposable(snackbarHostState)
        }
    }
}