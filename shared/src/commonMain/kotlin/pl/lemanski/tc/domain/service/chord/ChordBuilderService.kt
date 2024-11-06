package pl.lemanski.tc.domain.service.chord

import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.majorSeventh
import pl.lemanski.tc.domain.model.core.majorThird
import pl.lemanski.tc.domain.model.core.minorSeventh
import pl.lemanski.tc.domain.model.core.minorSixth
import pl.lemanski.tc.domain.model.core.minorThird
import pl.lemanski.tc.domain.model.core.natural
import pl.lemanski.tc.domain.model.core.perfectFifth
import pl.lemanski.tc.domain.model.core.tritone

internal class ChordBuilderService {
    private lateinit var baseNote: Note

    fun setBaseNote(note: Note) {
        baseNote = note
    }

    // Triad builder methods

    fun buildMinorTriad(): Chord = Chord(
        name = baseNote.name,
        noteValues = listOf(
            baseNote.natural,
            baseNote.minorThird(),
            baseNote.perfectFifth(),
        )
    )

    fun buildMajorTriad(): Chord = Chord(
        name = baseNote.name,
        noteValues = listOf(
            baseNote.natural,
            baseNote.majorThird(),
            baseNote.perfectFifth(),
        )
    )

    fun buildDiminishedTriad(): Chord = Chord(
        name = "${baseNote.name}b",
        noteValues = listOf(
            baseNote.natural,
            baseNote.minorThird(),
            baseNote.tritone(),
        )
    )

    fun buildAugmentedTriad(): Chord = Chord(
        name = "${baseNote.name}#",
        noteValues = listOf(
            baseNote.natural,
            baseNote.majorThird(),
            baseNote.minorSixth(),
        )
    )

    // 7th builder methods

    fun buildMinorSeventh(): Chord = Chord(
        name = "${baseNote.name}min7",
        noteValues = listOf(
            baseNote.natural,
            baseNote.minorThird(),
            baseNote.perfectFifth(),
            baseNote.minorSeventh(),
        )
    )

    fun buildMajorSeventh(): Chord = Chord(
        name = "${baseNote.name}maj7",
        noteValues = listOf(
            baseNote.natural,
            baseNote.majorThird(),
            baseNote.perfectFifth(),
            baseNote.majorSeventh(),
        )
    )

    fun buildDominantSeventh(): Chord = Chord(
        name = "${baseNote.name}7",
        noteValues = listOf(
            baseNote.natural,
            baseNote.majorThird(),
            baseNote.perfectFifth(),
            baseNote.majorSeventh(),
        )
    )
}