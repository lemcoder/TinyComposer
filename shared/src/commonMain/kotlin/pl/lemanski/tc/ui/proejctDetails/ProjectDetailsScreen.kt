package pl.lemanski.tc.ui.proejctDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.ToComposable

@Composable
internal fun ProjectDetailsScreen(
    isLoading: Boolean,
    projectName: String,
    projectRhythm: String,
    tempoInput: StateComponent.Input,
    chordsTextArea: StateComponent.Input,
    playButton: StateComponent.Button?,
    stopButton: StateComponent.Button?,
    backButton: StateComponent.Button,
    aiGenerateButton: StateComponent.Button,
    snackBar: StateComponent.SnackBar?,
) {
    LoaderScaffold(isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = backButton.onClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = projectName,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Text(
                text = projectRhythm,
                style = MaterialTheme.typography.bodyLarge
            )

            tempoInput.ToComposable()

            chordsTextArea.ToComposable()

            playButton?.let {
                IconButton(
                    onClick = it.onClick
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play"
                    )
                }
            }

            stopButton?.let {
                IconButton(
                    onClick = it.onClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Stop"
                    )
                }
            }

            Text(
                text = snackBar?.message ?: "",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}