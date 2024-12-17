package pl.lemanski.tc.data.model.project

import kotlinx.serialization.Serializable
import pl.lemanski.tc.data.model.core.ChordBeatsEntity
import pl.lemanski.tc.data.model.core.NoteBeatsEntity
import pl.lemanski.tc.data.model.core.toDomain
import pl.lemanski.tc.data.model.core.toEntity
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.utils.UUID

@Serializable
internal data class ProjectEntity(
    val id: String,
    val name: String,
    val bpm: Int,
    val rhythm: Int,
    val chords: List<ChordBeatsEntity>,
    val melody: List<NoteBeatsEntity>
)

internal fun Project.toEntity(): ProjectEntity = ProjectEntity(
    id = id.toString(),
    name = name,
    bpm = bpm,
    rhythm = rhythm.ordinal,
    chords = chords.map { ChordBeatsEntity(it.first.toEntity(), it.second) },
    melody = melody.map { NoteBeatsEntity(it.first.toEntity(), it.second) }
)

internal fun ProjectEntity.toDomain(): Project = Project(
    id = UUID(id),
    name = name,
    bpm = bpm,
    rhythm = Rhythm.entries.find { it.ordinal == rhythm } ?: Rhythm.FOUR_FOURS,
    chords = chords.map { it.chord.toDomain() to it.beats },
    melody = melody.map { it.note.toDomain() to it.beats }
)
