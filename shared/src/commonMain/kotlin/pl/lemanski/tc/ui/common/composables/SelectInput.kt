package pl.lemanski.tc.ui.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import pl.lemanski.tc.ui.common.StateComponent

@Composable
fun <T> StateComponent.SelectInput<T>.toComposable() {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = options.firstOrNull { it.value == value }?.name ?: hint,
            modifier = Modifier.clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onSelected(option.value)
                        expanded = false
                    }
                ) {
                    Text(option.name)
                }
            }
        }
    }
}