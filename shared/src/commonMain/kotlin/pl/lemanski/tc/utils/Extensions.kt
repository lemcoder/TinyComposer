package pl.lemanski.tc.utils

import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlin.math.round

/**
 * String extensions
 */
internal val String.Companion.EMPTY: String
    get() = ""

/**
 * ByteArray extensions
 */

internal fun ByteArray.toInt(): Int {
    if (this.size != Int.SIZE_BYTES) {
        throw IllegalArgumentException("Int must have ${Int.SIZE_BYTES} bytes")
    }

    with(Buffer()) {
        write(this@toInt)
        return readInt()
    }
}

/**
 * Int extensions
 */

internal fun Int.toByteArray(): ByteArray {
    with(Buffer()) {
        writeInt(this@toByteArray)
        return readByteArray()
    }
}

/**
 * Double extensions
 */
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

