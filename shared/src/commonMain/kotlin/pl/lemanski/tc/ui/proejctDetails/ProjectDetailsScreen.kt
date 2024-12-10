package pl.lemanski.tc.ui.proejctDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.ToComposable
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.ToComposable
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract.Tab
import pl.lemanski.tc.ui.proejctDetails.components.BottomBar
import pl.lemanski.tc.ui.proejctDetails.components.Wheel
import pl.lemanski.tc.ui.proejctDetails.components.chords.ChordDetailsBottomSheet
import pl.lemanski.tc.ui.proejctDetails.components.chords.ChordsTab
import pl.lemanski.tc.ui.proejctDetails.components.melody.MelodyTab
import pl.lemanski.tc.ui.proejctDetails.components.melody.NoteDetailsBottomSheet

@Composable
internal fun ProjectDetailsScreen(
    isLoading: Boolean,
    projectName: String,
    tabComponent: StateComponent.TabComponent<Tab>,
    playButton: StateComponent.Button?,
    stopButton: StateComponent.Button?,
    backButton: StateComponent.Button,
    addButton: StateComponent.Button,
    aiGenerateButton: StateComponent.Button,
    projectDetailsButton: StateComponent.Button,
    noteBeats: List<ProjectDetailsContract.State.NoteBeatsComponent>,
    chordBeats: List<ProjectDetailsContract.State.ChordBeatsComponent>,
    wheelPicker: ProjectDetailsContract.State.WheelPicker?,
    bottomSheet: ProjectDetailsContract.State.BottomSheet?,
    snackBar: StateComponent.SnackBar?,
) {
    LoaderScaffold(isLoading) { snackbarHostState ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = projectName,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = projectDetailsButton.onClick,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = "Back"
                    )
                }
            }

            tabComponent.ToComposable()

            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = if (wheelPicker != null) Alignment.Center else Alignment.TopCenter
            ) {
                if (wheelPicker != null) {
                    Wheel(
                        selected = wheelPicker.selectedValue,
                        options = wheelPicker.values,
                        onNoteSelected = wheelPicker.onValueSelected,
                        size = DpSize(this@BoxWithConstraints.maxWidth, this@BoxWithConstraints.maxWidth)
                    )
                } else {
                    when (tabComponent.selected.value) {
                        Tab.MELODY -> MelodyTab(noteBeats, maxWidth) // TODO
                        Tab.CHORDS -> ChordsTab(chordBeats, maxWidth) // TODO
                    }
                }
            }

            BottomBar(
                playButton = playButton,
                pauseButton = stopButton,
                addButton = addButton,
                aiGenerateButton = aiGenerateButton
            )
        }

        snackBar?.ToComposable(snackbarHostState)
    }

    bottomSheet?.let {
        when (it) {
            is ProjectDetailsContract.State.BottomSheet.NoteBottomSheet  -> NoteDetailsBottomSheet(it)
            is ProjectDetailsContract.State.BottomSheet.ChordBottomSheet -> ChordDetailsBottomSheet(it)
        }
    }
}

