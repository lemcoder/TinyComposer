package pl.lemanski.tc.ui.common.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import pl.lemanski.tc.ui.common.StateComponent

@Composable
fun StateComponent.Input.toComposable() {
    when (type) {
        StateComponent.Input.Type.NUMBER -> {
            TextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        onValueChange(newValue)
                    }
                },
                label = { Text(hint) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        StateComponent.Input.Type.TEXT -> {
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(hint) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }
    }
}