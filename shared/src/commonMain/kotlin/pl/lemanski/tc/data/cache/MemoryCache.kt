package pl.lemanski.tc.data.cache

import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlin.reflect.KClass

internal class MemoryCache {
    private val keyValueStore: MutableMap<String, Pair<KClass<*>, ByteArray>> = mutableMapOf()

    fun <T : Any> put(key: String, value: T) {
        when (value) {
            is Int       -> put(key, Int::class, value.toByteArray())
            is String    -> put(key, String::class, value.encodeToByteArray())
            is ByteArray -> put(key, ByteArray::class, value)
            else         -> throw IllegalArgumentException("Unsupported type")
        }
    }

    inline fun <reified T> get(key: String): T? {
        val value = keyValueStore[key]?.second ?: return null

        return when (T::class) {
            Int::class       -> value.toInt() as T
            String::class    -> value.decodeToString() as T
            ByteArray::class -> value as T
            else             -> null
        }
    }

    fun remove(key: String) {
        keyValueStore.remove(key)
    }

    fun clear() {
        keyValueStore.clear()
    }

    // ---

    private fun put(name: String, type: KClass<*>, data: ByteArray) {
        keyValueStore[name] = type to data
    }

    //---

    private fun ByteArray.toInt(): Int {
        if (this.size != Int.SIZE_BYTES) {
            throw IllegalArgumentException("Int must have ${Int.SIZE_BYTES} bytes")
        }

        with(Buffer()) {
            write(this@toInt)
            return readInt()
        }
    }

    private fun Int.toByteArray(): ByteArray {
        with(Buffer()) {
            writeInt(this@toByteArray)
            return readByteArray()
        }
    }
}