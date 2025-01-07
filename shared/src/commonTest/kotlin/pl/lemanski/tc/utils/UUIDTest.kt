package pl.lemanski.tc.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class UUIDTest {

    @Test
    fun should_equal() {
        val uuid1 = UUID.random()
        val uuid2 = UUID(uuid1.toString())
        assertEquals(uuid1, uuid2)
    }

    @Test
    fun should_not_equal() {
        val uuid1 = UUID.random()
        val uuid2 = UUID.random()
        assertNotEquals(uuid1, uuid2)
    }
}