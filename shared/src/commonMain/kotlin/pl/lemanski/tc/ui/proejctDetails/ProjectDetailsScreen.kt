package pl.lemanski.tc.ui.proejctDetails

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.ToComposable
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract.State.ChordBeatsComponent
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract.State.NoteBeatsComponent
import pl.lemanski.tc.ui.proejctDetails.components.BottomBar
import pl.lemanski.tc.ui.proejctDetails.components.chords.ChordsTab
import pl.lemanski.tc.ui.proejctDetails.components.melody.MelodyTab

private enum class Tab {
    CHORDS,
    MELODY
}

@Composable
internal fun ProjectDetailsScreen(
    isLoading: Boolean,
    projectName: String,
    playButton: StateComponent.Button?,
    stopButton: StateComponent.Button?,
    backButton: StateComponent.Button,
    aiGenerateButton: StateComponent.Button,
    snackBar: StateComponent.SnackBar?,
    noteBeats: List<NoteBeatsComponent>,
    addNoteButton: StateComponent.Button,
    chordBeats: List<ChordBeatsComponent>,
    addChordButton: StateComponent.Button,
) {
    LoaderScaffold(isLoading) { snackbarHostState ->
        var selectedTab by remember { mutableStateOf(Tab.CHORDS) }

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
                IconButton(onClick = backButton.onClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = projectName,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Back"
                    )
                }
            }

            TabRow(
                selectedTabIndex = Tab.entries.indexOf(selectedTab),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Tab.entries.map {
                    Tab(
                        text = { Text(it.name) },
                        selected = selectedTab == it,
                        onClick = { selectedTab = it }
                    )
                }
            }

            when (selectedTab) {
                Tab.CHORDS -> ChordsTab(chordBeatsComponents = chordBeats)
                Tab.MELODY -> MelodyTab(noteBeatsComponents = noteBeats)
            }

            BottomBar(
                playButton = playButton,
                pauseButton = stopButton,
                addButton = if (selectedTab == Tab.MELODY) addNoteButton else addChordButton,
                aiGenerateButton = aiGenerateButton
            )

            snackBar?.ToComposable(snackbarHostState)
        }
    }
}