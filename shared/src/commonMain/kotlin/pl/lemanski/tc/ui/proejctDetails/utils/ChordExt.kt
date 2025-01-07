package pl.lemanski.tc.ui.proejctDetails.utils

import pl.lemanski.tc.domain.model.core.Chord
import pl.lemanski.tc.ui.common.i18n.I18n

internal fun Chord.Type.getFullName(i18n: I18n): String = when (this) {
    Chord.Type.MINOR                   -> i18n.chords.minor
    Chord.Type.MAJOR                   -> i18n.chords.major
    Chord.Type.DIMINISHED              -> i18n.chords.diminished
    Chord.Type.AUGMENTED               -> i18n.chords.augmented
    Chord.Type.MAJOR_SEVENTH           -> i18n.chords.majorSeventh
    Chord.Type.MINOR_SEVENTH           -> i18n.chords.minorSeventh
    Chord.Type.DOMINANT_SEVENTH        -> i18n.chords.dominantSeventh
    Chord.Type.HALF_DIMINISHED_SEVENTH -> i18n.chords.halfDiminishedSeventh
    Chord.Type.DIMINISHED_SEVENTH      -> i18n.chords.diminishedSeventh
    Chord.Type.AUGMENTED_SEVENTH       -> i18n.chords.augmentedSeventh
    Chord.Type.MINOR_SIXTH             -> i18n.chords.minorSixth
    Chord.Type.MAJOR_SIXTH             -> i18n.chords.majorSixth
}