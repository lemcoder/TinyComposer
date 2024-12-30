package pl.lemanski.tc.ui.proejctDetails.composable.melody

import pl.lemanski.tc.domain.model.core.NoteBeats
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract

internal fun NoteBeats.toNoteBeatsComponent(
    id: Int,
    onNoteClick: (Int) -> Unit,
    onNoteDoubleClick: (Int) -> Unit,
    onNoteLongClick: (Int) -> Unit
) = List(this.second) { i ->
    ProjectDetailsContract.State.NoteComponent(
        id = id,
        isActive = false,
        isPrimary = i == 0,
        note = this.first,
        onNoteClick = onNoteClick,
        onNoteDoubleClick = onNoteDoubleClick,
        onNoteLongClick = onNoteLongClick
    )
}
