package pl.lemanski.tc.ui.proejctDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Wheel(
    selected: String,
    options: List<String>,
    onNoteSelected: (String) -> Unit,
    size: DpSize,
    modifier: Modifier = Modifier
) {
    val angleBetweenNotes = 360f / options.size

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        options.forEachIndexed { index, note ->
            val isSelected = note == selected
            val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            val angle = angleBetweenNotes * index
            val radians = angle.toDouble() * PI / 180
            val x = (((size.width / 2).value - 20) * cos(radians)).toFloat()
            val y = (((size.height / 2).value - 20) * sin(radians)).toFloat()

            Box(
                modifier = Modifier
                    .offset(x = x.dp, y = y.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onNoteSelected(note) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}