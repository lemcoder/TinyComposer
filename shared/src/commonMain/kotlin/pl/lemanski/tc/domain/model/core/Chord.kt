package pl.lemanski.tc.domain.model.core

/**
 * Represents a chord in a song as a sequence of notes.
 */
data class Chord(
    val name: String,
    val notes: List<Note>
)