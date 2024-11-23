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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.ToComposable

@Composable
internal fun ProjectDetailsScreen(
    isLoading: Boolean,
    title: String,
    projectName: String,
    projectBpm: String,
    projectRhythm: String,
    chordsTextArea: StateComponent.Input,
    playButton: StateComponent.Button?,
    stopButton: StateComponent.Button?,
    aiGenerateButton: StateComponent.Button,
    errorSnackBar: StateComponent.SnackBar?,
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )

                IconButton({}) {}
            }
        }
    }
}