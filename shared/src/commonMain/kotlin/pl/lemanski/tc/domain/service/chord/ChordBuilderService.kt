package pl.lemanski.tc.domain.service.chord

import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.domain.model.core.Note
import pl.lemanski.tc.domain.model.core.majorThird
import pl.lemanski.tc.domain.model.core.minorThird

// TODO enable building custom chords
internal class ChordBuilderService {

    private lateinit var baseNote: Note

    fun setBaseNote(note: Note) {
        baseNote = note
    }

    // Triad builder methods

    fun buildMinorTriad(): Chord = Chord(
        name = "${baseNote.name}min",
        notes = listOf(
            baseNote,
            baseNote.minorThird(),
            baseNote.minorThird().majorThird(),
        )
    )

    fun buildMajorTriad(): Chord = Chord(
        name = baseNote.name,
        notes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().minorThird(),
        )
    )

    fun buildDiminishedTriad(): Chord = Chord(
        name = "${baseNote.name}dim",
        notes = listOf(
            baseNote,
            baseNote.minorThird(),
            baseNote.minorThird().minorThird(),
        )
    )

    fun buildAugmentedTriad(): Chord = Chord(
        name = "${baseNote.name}aug",
        notes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().majorThird(),
        )
    )

    // 7th builder methods

    fun buildMinorSeventh(): Chord = Chord(
        name = "${baseNote.name}min7",
        notes = listOf(
            baseNote,
            baseNote.minorThird(),
            baseNote.minorThird().majorThird(),
            baseNote.minorThird().majorThird().minorThird(),
        )
    )

    fun buildMajorSeventh(): Chord = Chord(
        name = "${baseNote.name}maj7",
        notes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().minorThird(),
            baseNote.majorThird().minorThird().majorThird(),
        )
    )

    fun buildDominantSeventh(): Chord = Chord(
        name = "${baseNote.name}7",
        notes = listOf(
            baseNote,
            baseNote.majorThird(),
            baseNote.majorThird().minorThird(),
            baseNote.majorThird().minorThird().minorThird(),
        )
    )
}