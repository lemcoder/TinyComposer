package pl.lemanski.tc.domain.model.core

/**
 * Represents a chord in a song as a sequence of notes.
 */


data class Chord(
    val type: Type,
    val notes: List<Note>
) {
    val name = "${notes.first().name}${type.notation}"

    enum class Type(val notation: String) {
        MINOR("min"),
        MAJOR("maj"),
        DIMINISHED("dim"),
        AUGMENTED("aug"),
        MAJOR_SEVENTH("maj7"),
        MINOR_SEVENTH("min7"),
        DOMINANT_SEVENTH("7"),
        HALF_DIMINISHED_SEVENTH("o7"),
        DIMINISHED_SEVENTH("dim7"),
        AUGMENTED_SEVENTH("aug7"),
        MINOR_SIXTH("min6"),
        MAJOR_SIXTH("maj6"),
    }
}

internal fun Chord.Type.build(baseNote: Note): Chord {
    return when (this) {
        Chord.Type.MINOR                   -> buildMinorTriad(baseNote)
        Chord.Type.MAJOR                   -> buildMajorTriad(baseNote)
        Chord.Type.DIMINISHED              -> buildDiminishedTriad(baseNote)
        Chord.Type.AUGMENTED               -> buildAugmentedTriad(baseNote)
        Chord.Type.MAJOR_SEVENTH           -> buildMajorSeventh(baseNote)
        Chord.Type.MINOR_SEVENTH           -> buildMinorSeventh(baseNote)
        Chord.Type.DOMINANT_SEVENTH        -> buildDominantSeventh(baseNote)
        Chord.Type.HALF_DIMINISHED_SEVENTH -> buildHalfDiminishedSeventh(baseNote)
        Chord.Type.DIMINISHED_SEVENTH      -> buildDiminishedSeventh(baseNote)
        Chord.Type.AUGMENTED_SEVENTH       -> buildAugmentedSeventh(baseNote)
        Chord.Type.MINOR_SIXTH             -> buildMinorSixth(baseNote)
        Chord.Type.MAJOR_SIXTH             -> buildMajorSixth(baseNote)
    }
}

// ---


internal fun buildMinorTriad(baseNote: Note): Chord = Chord(
    type = Chord.Type.MINOR,
    notes = listOf(
        baseNote,
        baseNote.minorThird(),
        baseNote.minorThird().majorThird(),
    )
)

internal fun buildMajorTriad(baseNote: Note): Chord = Chord(
    type = Chord.Type.MAJOR,
    notes = listOf(
        baseNote,
        baseNote.majorThird(),
        baseNote.majorThird().minorThird(),
    )
)

internal fun buildDiminishedTriad(baseNote: Note): Chord = Chord(
    type = Chord.Type.DIMINISHED,
    notes = listOf(
        baseNote,
        baseNote.minorThird(),
        baseNote.minorThird().minorThird(),
    )
)

internal fun buildAugmentedTriad(baseNote: Note): Chord = Chord(
    type = Chord.Type.AUGMENTED,
    notes = listOf(
        baseNote,
        baseNote.majorThird(),
        baseNote.majorThird().majorThird(),
    )
)

// 6th builder methods

internal fun buildMinorSixth(baseNote: Note): Chord = Chord(
    type = Chord.Type.MINOR_SIXTH,
    notes = listOf(
        baseNote,
        baseNote.minorThird(),
        baseNote.minorThird().majorThird(), // Perfect fifth
        baseNote.minorThird().majorThird().majorSecond() // Major sixth
    )
)

internal fun buildMajorSixth(baseNote: Note): Chord = Chord(
    type = Chord.Type.MAJOR_SIXTH,
    notes = listOf(
        baseNote,
        baseNote.majorThird(),
        baseNote.majorThird().minorThird(), // Perfect fifth
        baseNote.majorThird().minorThird().majorSecond() // Major sixth
    )
)

// 7th builder methods

internal fun buildMinorSeventh(baseNote: Note): Chord = Chord(
    type = Chord.Type.MINOR_SEVENTH,
    notes = listOf(
        baseNote,
        baseNote.minorThird(),
        baseNote.minorThird().majorThird(),
        baseNote.minorThird().majorThird().minorThird(),
    )
)

internal fun buildMajorSeventh(baseNote: Note): Chord = Chord(
    type = Chord.Type.MAJOR_SEVENTH,
    notes = listOf(
        baseNote,
        baseNote.majorThird(),
        baseNote.majorThird().minorThird(),
        baseNote.majorThird().minorThird().majorThird(),
    )
)

internal fun buildDominantSeventh(baseNote: Note): Chord = Chord(
    type = Chord.Type.DOMINANT_SEVENTH,
    notes = listOf(
        baseNote,
        baseNote.majorThird(),
        baseNote.majorThird().minorThird(), // Perfect fifth
        baseNote.majorThird().minorThird().minorThird() // Minor seventh
    )
)

internal fun buildAugmentedSeventh(baseNote: Note): Chord = Chord(
    type = Chord.Type.AUGMENTED_SEVENTH,
    notes = listOf(
        baseNote,
        baseNote.majorThird(),
        baseNote.majorThird().majorThird(), // Augmented fifth
        baseNote.majorThird().majorThird().minorThird() // Minor seventh
    )
)

internal fun buildHalfDiminishedSeventh(baseNote: Note): Chord = Chord(
    type = Chord.Type.HALF_DIMINISHED_SEVENTH,
    notes = listOf(
        baseNote,
        baseNote.minorThird(),
        baseNote.minorThird().minorThird(), // Diminished fifth
        baseNote.minorThird().minorThird().majorThird() // Minor seventh
    )
)

internal fun buildDiminishedSeventh(baseNote: Note): Chord = Chord(
    type = Chord.Type.DIMINISHED_SEVENTH,
    notes = listOf(
        baseNote,
        baseNote.minorThird(),
        baseNote.minorThird().minorThird(), // Diminished fifth
        baseNote.minorThird().minorThird().minorThird() // Diminished seventh
    )
)