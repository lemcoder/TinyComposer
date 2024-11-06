package pl.lemanski.tc.domain.model.core

/**
 * Represents a note in a musical context.
 * A note can have a value of 0-127, where 0 is the lowest and 127 is the highest.
 */
internal sealed class Note(
    val name: String,
    open val octave: Int
) {
    init {
        @Suppress("LeakingThis") // We fine
        require(octave in 0..9)
    }

    data class C(override val octave: Int) : Note("C", octave)
    data class D(override val octave: Int) : Note("D", octave)
    data class E(override val octave: Int) : Note("E", octave)
    data class F(override val octave: Int) : Note("F", octave)
    data class G(override val octave: Int) : Note("G", octave)
    data class A(override val octave: Int) : Note("A", octave)
    data class B(override val octave: Int) : Note("B", octave)
}

internal val Note.natural: Int
    get() = when (this) {
        is Note.C -> 0
        is Note.D -> 2
        is Note.E -> 4
        is Note.F -> 5
        is Note.G -> 7
        is Note.A -> 9
        is Note.B -> 11
    } + octave * 12

//--- Flats and sharps

internal fun Note.flat(): Int = this.natural - 1

internal fun Note.sharp(): Int = this.natural + 1

//--- Intervals

internal fun Note.minorSecond(): Int = this.natural + 1

internal fun Note.majorSecond(): Int = this.natural + 2

internal fun Note.minorThird(): Int = this.natural + 3

internal fun Note.majorThird(): Int = this.natural + 4

internal fun Note.perfectFourth(): Int = this.natural + 5

internal fun Note.tritone(): Int = this.natural + 6

internal fun Note.perfectFifth(): Int = this.natural + 7

internal fun Note.minorSixth(): Int = this.natural + 8

internal fun Note.majorSixth(): Int = this.natural + 9

internal fun Note.minorSeventh(): Int = this.natural + 10

internal fun Note.majorSeventh(): Int = this.natural + 11