package pl.lemanski.tc.ui.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.lemanski.tc.ui.common.StateComponent

@Composable
internal fun <T> StateComponent.TabComponent<T>.ToComposable() {
    TabRow(
        selectedTabIndex = options.indexOf(selected),
        modifier = Modifier.fillMaxWidth(),
    ) {
        options.map {
            Tab(
                text = { Text(it.name) },
                selected = selected.value == it.value,
                onClick = { onTabSelected(it.value) }
            )
        }
    }
}