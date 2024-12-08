package pl.lemanski.tc.ui.proejctDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.lemanski.tc.ui.common.StateComponent

@Composable
fun BottomBar(
    playButton: StateComponent.Button?,
    pauseButton: StateComponent.Button?,
    addButton: StateComponent.Button,
    aiGenerateButton: StateComponent.Button,
    modifier: Modifier = Modifier
) {
    Box {
        HorizontalDivider(modifier = modifier.fillMaxWidth())

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

            IconButton(onClick = addButton.onClick) {
                Icon(
                    imageVector = Icons.Default.Add, // TODO add add icon
                    contentDescription = "Play"
                )
            }
        }
    }
}

