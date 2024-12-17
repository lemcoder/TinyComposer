package pl.lemanski.tc.utils

import kotlin.test.Test

class UUIDTest {

    @Test
    fun should_equal() {
        val uuid1 = UUID.random()
        val uuid2 = UUID(uuid1.toString())
        assert(uuid1 == uuid2)
    }

    @Test
    fun should_not_equal() {
        val uuid1 = UUID.random()
        val uuid2 = UUID.random()
        assert(uuid1 != uuid2)
    }
}