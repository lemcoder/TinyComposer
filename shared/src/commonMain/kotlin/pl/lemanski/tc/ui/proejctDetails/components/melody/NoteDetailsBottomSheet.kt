package pl.lemanski.tc.ui.proejctDetails.components.melody

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.composables.SliderValuePicker
import pl.lemanski.tc.ui.common.composables.ToComposable
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteDetailsBottomSheet(
    state: ProjectDetailsContract.State.BottomSheet.NoteBottomSheet,
) {
    ModalBottomSheet(
        onDismissRequest = state.onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.durationValuePicker.ToComposable()

            SliderValuePicker(
                state = state.velocityValuePicker,
                maxValue = 127
            )

            state.octaveValuePicker.ToComposable()
        }
    }
}