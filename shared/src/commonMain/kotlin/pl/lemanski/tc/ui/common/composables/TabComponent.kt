package pl.lemanski.tc.ui.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.StateComponent

@Composable
internal fun <T> StateComponent.TabComponent<T>.ToComposable() {
    val selectedTabIndex = options.indexOf(selected)

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth(),
        indicator = { },
    ) {
        options.map {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTabSelected(it.value) }
                    )
                    .clip(CircleShape)
                    .background(if (selected.value == it.value) MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) else MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it.name,
                    modifier = Modifier.padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    )
                )
            }
        }
    }
}