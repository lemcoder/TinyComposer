package pl.lemanski.tc.ui

import pl.lemanski.tc.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}