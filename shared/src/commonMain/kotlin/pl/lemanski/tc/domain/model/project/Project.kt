package pl.lemanski.tc.domain.model.project

import pl.lemanski.tc.domain.model.song.Song
import pl.lemanski.tc.utils.UUID

data class Project(
    val id: UUID,
    val name: String,
    val lengthInBars: Int,
    val song: Song
)