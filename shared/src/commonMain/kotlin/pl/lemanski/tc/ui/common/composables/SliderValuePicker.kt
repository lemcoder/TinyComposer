package pl.lemanski.tc.ui.common.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.lemanski.tc.ui.common.StateComponent

@Composable
internal fun SliderValuePicker(
    state: StateComponent.ValuePicker,
    maxValue: Int = 100
) {
    Column {
        Text(
            text = state.label,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = state.value / maxValue.toFloat(),
                steps = 8,
                onValueChange = { state.onValueChange((it * maxValue).toInt()) },
            )
        }
    }
}