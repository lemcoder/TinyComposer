package pl.lemanski.tc.ui.common.composables

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import pl.lemanski.tc.ui.common.StateComponent

@Composable
fun StateComponent.Button.toComposable() {
    Button(
        onClick = onClick,
    ) {
        Text(text = text)
    }
}