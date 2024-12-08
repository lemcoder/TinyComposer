package pl.lemanski.tc.ui.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import pl.lemanski.tc.ui.common.StateComponent

@Composable
internal fun <T> StateComponent.SelectInput<T>.ToComposable() {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = Modifier.clickable { expanded = true }
        ) {
            val colors = OutlinedTextFieldDefaults.colors()

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = selected.name,
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors().copy(
                    disabledPrefixColor = if (expanded) colors.focusedPrefixColor else colors.unfocusedPrefixColor,
                    disabledContainerColor = if (expanded) colors.focusedContainerColor else colors.unfocusedContainerColor,
                    disabledSuffixColor = if (expanded) colors.focusedSuffixColor else colors.unfocusedSuffixColor,
                    disabledTextColor = if (expanded) colors.focusedTextColor else colors.unfocusedTextColor,
                    disabledLabelColor = if (expanded) colors.focusedLabelColor else colors.unfocusedLabelColor,
                    disabledIndicatorColor = if (expanded) colors.focusedIndicatorColor else colors.unfocusedIndicatorColor,
                    disabledPlaceholderColor = if (expanded) colors.focusedPlaceholderColor else colors.unfocusedPlaceholderColor,
                    disabledLeadingIconColor = if (expanded) colors.focusedLeadingIconColor else colors.unfocusedLeadingIconColor,
                    disabledTrailingIconColor = if (expanded) colors.focusedTrailingIconColor else colors.unfocusedTrailingIconColor,
                    disabledSupportingTextColor = if (expanded) colors.focusedSupportingTextColor else colors.unfocusedSupportingTextColor,
                ),
                onValueChange = { },
                label = { Text(label) },
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                    text = {
                        Text(option.name)
                    }
                )
            }
        }
    }
}