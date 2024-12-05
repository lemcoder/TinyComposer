package pl.lemanski.tc.ui.proejctDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.domain.model.project.ChordBeats
import pl.lemanski.tc.domain.model.project.NoteBeats
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.ToComposable
import pl.lemanski.tc.ui.common.composables.LoaderScaffold

private enum class Tab {
    CHORDS,
    MELODY
}

@Composable
fun ProjectDetailsScreen(
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
    LoaderScaffold(isLoading) { snackbarHostState ->
        var selectedTab by remember { mutableStateOf<Tab>(Tab.CHORDS) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                ) { // TODO options button
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
                Tab.CHORDS -> ChordsTab(
                    chordBeats = emptyList(),
                    onChordDoubleClick = {},
                    onChordLongClick = {},
                    onChordAddClicked = {}
                )
                Tab.MELODY -> MelodyTab(
                    noteBeats = emptyList(),
                    onNoteDoubleClick = {},
                    onNoteLongClick = {},
                    onNoteAddClicked = {}
                )
            }

            BottomBar(
                playButton = playButton,
                pauseButton = stopButton,
                aiGenerateButton = aiGenerateButton
            )

            snackBar?.ToComposable(snackbarHostState)
        }
    }
}

//---
// TODO

@Composable
private fun ColumnScope.ChordsTab(
    chordBeats: List<ChordBeats>,
    onChordDoubleClick: (ChordBeats) -> Unit,
    onChordLongClick: (ChordBeats) -> Unit,
    onChordAddClicked: () -> Unit // TODO add wheel picker
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Red).weight(1f)
    )
}

@Composable
private fun ChordDetailsBottomSheet(

) {

}


//---

@Composable
private fun ColumnScope.MelodyTab(
    noteBeats: List<NoteBeats>,
    onNoteDoubleClick: (NoteBeats) -> Unit,
    onNoteLongClick: (NoteBeats) -> Unit,
    onNoteAddClicked: () -> Unit // TODO add wheel picker
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Blue).weight(1f)
    )
}

@Composable
private fun NoteDetailsBottomSheet(

) {

}

//---

@Composable
fun BottomBar(
    playButton: StateComponent.Button?,
    pauseButton: StateComponent.Button?,
    aiGenerateButton: StateComponent.Button,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = aiGenerateButton.onClick) {
                Icon(
                    imageVector = Icons.Default.Star, // TODO add gemini icon
                    contentDescription = "Play"
                )
            }

            playButton?.let {
                IconButton(onClick = it.onClick) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play"
                    )
                }
            }

            pauseButton?.let {
                IconButton(onClick = it.onClick) {
                    Icon(
                        imageVector = Icons.Default.Close, // TODO add pause icon
                        contentDescription = "Play"
                    )
                }
            }

            Spacer(modifier = Modifier.size(ButtonDefaults.MinWidth)) // STUB
        }
    }
}

