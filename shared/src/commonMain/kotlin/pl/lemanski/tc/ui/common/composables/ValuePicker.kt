package pl.lemanski.tc.ui.common.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import pl.lemanski.tc.ui.common.StateComponent

@Composable
internal fun StateComponent.ValuePicker.ToComposable() {
    Column {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { this@ToComposable.onValueChange(value - 1) }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Decrease value"
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = this@ToComposable.value.toString(),
                textAlign = TextAlign.Center,
            )
            IconButton(
                onClick = { this@ToComposable.onValueChange(value + 1) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase value"
                )
            }
        }
    }
}