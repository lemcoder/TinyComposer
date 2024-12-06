package pl.lemanski.tc.ui.proejctDetails.components.chords

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.composables.MultiRowLayout
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract.State.ChordBeatsComponent

@Composable
internal fun ColumnScope.ChordsTab(
    chordBeatsComponents: List<ChordBeatsComponent>,
) {
    BoxWithConstraints(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        MultiRowLayout(
            modifier = Modifier
        ) {
            chordBeatsComponents.forEach {
                it.ChordBeatItem(itemWidth = this@BoxWithConstraints.maxWidth / 5)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChordBeatsComponent.ChordBeatItem(
    itemWidth: Dp
) {
    val (chord, beats) = remember { chordBeats }
    val color = remember(chord.type.ordinal) {
        Color(
            red = 0f,
            green = (id.toFloat() / 20).coerceIn(0f, 1f),
            blue = (id.toFloat() / 10).coerceIn(0f, 1f),
            alpha = (id.toFloat() / 10).coerceIn(0f, 1f)
        )
    }

    Box(
        modifier = Modifier
            .height(48.dp)
            .width(itemWidth * beats)
            .padding(horizontal = 2.dp)
            .clip(OutlinedTextFieldDefaults.shape)
            .background(color)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onLongClick = { onChordLongClick(id) },
                onDoubleClick = { onChordDoubleClick(id) },
            ) {
                onChordClick(id)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = chord.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
