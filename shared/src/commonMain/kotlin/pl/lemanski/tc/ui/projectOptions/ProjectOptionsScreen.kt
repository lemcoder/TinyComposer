package pl.lemanski.tc.ui.projectOptions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
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
internal fun ProjectOptionsScreen(
    isLoading: Boolean,
    title: String,
    tempoInput: StateComponent.Input,
    notesPresetSelect: StateComponent.SelectInput<Int>,
    chordsPresetSelect: StateComponent.SelectInput<Int>,
    exportButton: StateComponent.Button,
    snackBar: StateComponent.SnackBar?,
) {
    LoaderScaffold(isLoading) { snackbarHostState ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.Top
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            tempoInput.ToComposable()

            notesPresetSelect.ToComposable()

            chordsPresetSelect.ToComposable()

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = exportButton.onClick
            ) {
                Text(exportButton.text)
            }

            snackBar?.ToComposable(snackbarHostState)
        }
    }
}

