package pl.lemanski.tc.utils

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Wrapper class for unstable UUID type.
 * TODO replace with kotlinx.uuid when it's stable
 */
@OptIn(ExperimentalUuidApi::class)
class UUID(
    val value: ByteArray
) {
    companion object {
        fun random(): UUID {
            return UUID(Uuid.random().toByteArray())
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun toString(): String {
        return value.toHexString()
    }

    override fun hashCode(): Int {
        return value.contentHashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UUID

        return value.contentEquals(other.value)
    }
}