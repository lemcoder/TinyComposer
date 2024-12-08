package pl.lemanski.tc.ui.common.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import pl.lemanski.tc.ui.common.StateComponent

@Composable
fun StateComponent.ValuePicker.ToComposable() {
    val value = remember { this.value }

    Row(
        modifier = Modifier.fillMaxWidth()
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
            text = this@ToComposable.value.toString(),
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