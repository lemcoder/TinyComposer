package pl.lemanski.tc.domain.model.core

import pl.lemanski.tc.utils.exception.InvalidNoteException

/**
 * Represents a note in a musical context with a duration.
 */
typealias NoteBeats = Pair<Note, Int>

/**
 * Represents a note in a musical context.
 * A note can have a value of 0-127, where 0 is the lowest and 127 is the highest.
 */
data class Note(
    val value: Int,
    val velocity: Int = 127,
) {
    init {
        if (value !in 0..127) {
            throw InvalidNoteException("Note value must be between 0 and 127")
        }
    }

    val nameWithOctave: String
        get() = (noteLookupTable.note(value % 12) ?: throw InvalidNoteException("Invalid note $value")) + octave.toString()
    val name: String
        get() = noteLookupTable.note(value % 12) ?: throw InvalidNoteException("Invalid note $value")
    val octave: Int
        get() = value / 11

    companion object {
        private val noteLookupTable = NoteLookupTable()

        fun fromString(note: String): Note? {
            for (i in 0..11) {
                if (noteLookupTable.note(i) == note) {
                    return Note(i + 60)
                }
            }

            return null
        }
    }


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

    private class NoteLookupTable {
        private val notes = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

        // TODO add flats notation
        fun note(value: Int): String? = notes.getOrNull(value)
    }
}

internal fun Note.changeOctave(newOctave: Int): Note = Note(
    value = this.value + (newOctave * 12),
    velocity = this.velocity
)

//--- Flats and sharps

internal fun Note.flat(): Note = Note(this.value - 1)

internal fun Note.sharp(): Note = Note(this.value + 1)

//--- Intervals

internal fun Note.minorSecond(): Note = Note(this.value + 1)

internal fun Note.majorSecond(): Note = Note(this.value + 2)

internal fun Note.minorThird(): Note = Note(this.value + 3)

internal fun Note.majorThird(): Note = Note(this.value + 4)