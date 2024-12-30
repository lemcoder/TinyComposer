package pl.lemanski.tc.ui.proejctDetails.composable.chords

import pl.lemanski.tc.domain.model.core.ChordBeats
import pl.lemanski.tc.ui.proejctDetails.ProjectDetailsContract

internal fun ChordBeats.toChordComponents(
    id: Int,
    onChordClick: (Int) -> Unit,
    onChordDoubleClick: (Int) -> Unit,
    onChordLongClick: (Int) -> Unit
): List<ProjectDetailsContract.State.ChordComponent> = List(this.second) { i ->
    ProjectDetailsContract.State.ChordComponent(
        id = id,
        isActive = false,
        isPrimary = i == 0,
        chord = this.first,
        onChordClick = onChordClick,
        onChordDoubleClick = onChordDoubleClick,
        onChordLongClick = onChordLongClick
    )
}
