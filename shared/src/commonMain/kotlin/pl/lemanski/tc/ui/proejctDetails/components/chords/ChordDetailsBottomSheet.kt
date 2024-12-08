package pl.lemanski.tc.ui.proejctDetails.components.chords

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.composables.ToComposable
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChordDetailsBottomSheet(
    state: ProjectDetailsContract.State.BottomSheet.ChordBottomSheet,
) {
    ModalBottomSheet(
        onDismissRequest = state.onDismiss,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            state.durationValuePicker.ToComposable()
        }
    }
}