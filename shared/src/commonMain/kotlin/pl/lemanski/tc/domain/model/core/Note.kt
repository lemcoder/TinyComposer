package pl.lemanski.tc.domain.model.core

import pl.lemanski.tc.exception.InvalidNoteException

/**
 * Represents a note in a musical context.
 * A note can have a value of 0-127, where 0 is the lowest and 127 is the highest.
 */
class Note(val value: Int) {
    init {
        if (value !in 0..127) {
            throw InvalidNoteException("Note value must be between 0 and 127")
        }
    }

    private val noteLookupTable = NoteLookupTable()

    val name: String
        get() = noteLookupTable.note(value % 12) ?: throw InvalidNoteException("Invalid note $value")
    val octave: Int
        get() = value / 11

    // ---

    override fun equals(other: Any?): Boolean {
        return this.value == ((other as? Note)?.value ?: -1)
    }

    override fun hashCode(): Int {
        var result = value
        result = 31 * result + noteLookupTable.hashCode()
        return result
    }

    // ---

    private inner class NoteLookupTable {

        private var notation: Notation = Notation.STANDARD

        // TODO add flats notation
        fun note(value: Int): String? = when (value) {
            0    -> if (notation == Notation.STANDARD) "C" else "Do"
            1    -> if (notation == Notation.STANDARD) "C#" else "Di"
            2    -> if (notation == Notation.STANDARD) "D" else "Re"
            3    -> if (notation == Notation.STANDARD) "D#" else "Ri"
            4    -> if (notation == Notation.STANDARD) "E" else "Mi"
            5    -> if (notation == Notation.STANDARD) "F" else "Fa"
            6    -> if (notation == Notation.STANDARD) "F#" else "Fi"
            7    -> if (notation == Notation.STANDARD) "G" else "Sol"
            8    -> if (notation == Notation.STANDARD) "G#" else "Si"
            9    -> if (notation == Notation.STANDARD) "A" else "La"
            10   -> if (notation == Notation.STANDARD) "A#" else "Li"
            11   -> if (notation == Notation.STANDARD) "B" else "Ti"
            else -> null
        }

        fun setNotation(notation: Notation) {
            this.notation = notation
        }
    }
}

//--- Flats and sharps

internal fun Note.flat(): Note = Note(this.value - 1)

internal fun Note.sharp(): Note = Note(this.value + 1)

//--- Intervals

internal fun Note.minorSecond(): Note = Note(this.value + 1)

internal fun Note.majorSecond(): Note = Note(this.value + 2)

internal fun Note.minorThird(): Note = Note(this.value + 3)

internal fun Note.majorThird(): Note = Note(this.value + 4)