package pl.lemanski.tc.ui.proejctDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.ToComposable
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.ToComposable
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract.Tab
import pl.lemanski.tc.ui.proejctDetails.composable.BottomBar
import pl.lemanski.tc.ui.proejctDetails.composable.chords.ChordsTab
import pl.lemanski.tc.ui.proejctDetails.composable.melody.MelodyTab

@Composable
internal fun ProjectDetailsScreen(
    isLoading: Boolean,
    projectName: String,
    tabComponent: StateComponent.TabComponent<Tab>,
    playButton: StateComponent.Button?,
    stopButton: StateComponent.Button?,
    aiGenerateButton: StateComponent.Button,
    projectDetailsButton: StateComponent.Button,
    pageState: ProjectDetailsContract.State.PageState,
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

            Box(
                modifier = Modifier.weight(1f)
            ) {
                when (tabComponent.selected.value) {
                    Tab.MELODY -> MelodyTab(pageState)
                    Tab.CHORDS -> ChordsTab(pageState)
                }
            }

            BottomBar(
                playButton = playButton,
                pauseButton = stopButton,
                addButton = pageState.addButton.takeIf { pageState.wheelPicker == null }, // FIXME
                closeButton = pageState.addButton.takeIf { pageState.wheelPicker != null }, // FIXME
                aiGenerateButton = aiGenerateButton
            )
        }

        snackBar?.ToComposable(snackbarHostState)
    }
}
