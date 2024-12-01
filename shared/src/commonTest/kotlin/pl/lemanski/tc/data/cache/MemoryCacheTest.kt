package pl.lemanski.tc.data.cache

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MemoryCacheTest {
    private val cache = MemoryCache()

    @Test
    fun set_and_get_int() {
        cache.set("key1", 42)
        val value: Int? = cache.get("key1")
        assertNotNull(value)
        assertEquals(42, value)
    }

    @Test
    fun set_and_get_string() {
        cache.set("key2", "Hello, World!")
        val value: String? = cache.get("key2")
        println(value)
        assertNotNull(value)
        assertEquals("Hello, World!", value)
    }

    @Test
    fun set_and_get_byte_array() {
        val byteArray = byteArrayOf(1, 2, 3, 4)
        cache.set("key3", byteArray)
        val value: ByteArray? = cache.get("key3")
        assertNotNull(value)
        assertTrue(byteArray contentEquals value)
    }

    @Test
    fun get_returns_null_for_non_existent_key() {
        val value: Int? = cache.get("nonExistentKey")
        assertNull(value)
    }

    @Test
    fun throws_exception_for_unsupported_type() {
        val exception = assertFailsWith<IllegalArgumentException> {
            cache.set("key5", 12.34) // Double is not supported
        }
        assertEquals("Unsupported type", exception.message)
    }

    @Test
    fun can_store_and_retrieve_multiple_values() {
        cache.set("key1", 100)
        cache.set("key2", "Kotlin")
        cache.set("key3", byteArrayOf(5, 6, 7))

        val int1: Int? = cache.get("key1")
        val string1: String? = cache.get("key2")
        val byteArray1: ByteArray? = cache.get("key3")

        assertEquals(100, int1)
        assertEquals("Kotlin", string1)
        assertNotNull(byteArray1)
        assertTrue(byteArrayOf(5, 6, 7) contentEquals byteArray1)
    }

    @Test
    fun overwriting_a_key_replaces_the_value() {
        cache.set("key1", 123)
        cache.set("key1", 456) // Overwriting the value

        val value: Int? = cache.get("key1")
        assertEquals(456, value)
    }

    @Test
    fun remove_deletes_a_key() {
        cache.set("key1", 42)
        cache.remove("key1")

        val value: Int? = cache.get("key1")
        assertNull(value)
    }

    @Test
    fun clear_deletes_all_keys() {
        cache.set("key1", 100)
        cache.set("key2", "Hello")
        cache.set("key3", byteArrayOf(1, 2, 3))

        cache.clear()

        assertNull(cache.get<Int>("key1"))
        assertNull(cache.get<String>("key2"))
        assertNull(cache.get<ByteArray>("key3"))
    }
}
