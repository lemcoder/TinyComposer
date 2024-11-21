package pl.lemanski.tc.domain.model.project

import pl.lemanski.tc.ui.common.i18n.I18n

enum class Rhythm(val beatsPerBar: Int) {
    FOUR_FOURS(4),
    THREE_FOURS(3)
}

internal fun Rhythm.name(i18n: I18n): String = when (this) {
    Rhythm.FOUR_FOURS  -> i18n.rhythm.fourFours
    Rhythm.THREE_FOURS -> i18n.rhythm.threeFours
}