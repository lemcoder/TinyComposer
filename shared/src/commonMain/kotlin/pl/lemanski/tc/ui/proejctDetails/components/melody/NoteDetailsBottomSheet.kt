package pl.lemanski.tc.ui.proejctDetails.components.melody

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import pl.lemanski.tc.ui.common.composables.ToComposable
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteDetailsBottomSheet(
    state: ProjectDetailsContract.State.BottomSheet.NoteBottomSheet,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column {
            state.durationValuePicker.ToComposable()
            state.velocityValuePicker.ToComposable()
        }
    }
}