package pl.lemanski.tc.data.model.core

import kotlinx.serialization.Serializable
import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.build

@Serializable
internal data class ChordBeatsEntity(
    val chord: ChordEntity,
    val beats: Int
)

internal fun ChordBeatsEntity.toDomain(): Pair<Chord, Int> = chord.toDomain() to beats

internal fun Pair<Chord, Int>.toEntity(): ChordBeatsEntity = ChordBeatsEntity(
    chord = first.toEntity(),
    beats = second
)

@Serializable
internal data class ChordEntity(
    val type: String,
    val baseNote: NoteEntity,
)

internal fun Chord.toEntity(): ChordEntity = ChordEntity(
    type = type.name,
    baseNote = notes.first().toEntity(), // FIXME this will not work if chord is in different inversion
)

internal fun ChordEntity.toDomain(): Chord = Chord.Type.valueOf(type).build(baseNote.toDomain())
