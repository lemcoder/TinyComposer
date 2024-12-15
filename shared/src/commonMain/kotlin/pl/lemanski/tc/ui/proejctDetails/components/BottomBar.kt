package pl.lemanski.tc.ui.proejctDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.icons.Ai
import pl.lemanski.tc.ui.common.icons.Pause

@Composable
internal fun BottomBar(
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
                    imageVector = Icons.Outlined.Ai,
                    contentDescription = "Ai generate"
                )
            }

            playButton?.let {
                CircleButton(
                    onClick = it.onClick,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = "Play"
                    )
                }
            }

            pauseButton?.let {
                CircleButton(
                    onClick = it.onClick,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Pause,
                        contentDescription = "pause"
                    )
                }
            }

            IconButton(onClick = addButton.onClick) {
                Icon(
                    imageVector = Icons.Default.Add, // TODO add add icon
                    contentDescription = "Add new element"
                )
            }
        }
    }
}

@Composable
private fun CircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            .padding(4.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}
