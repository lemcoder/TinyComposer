package pl.lemanski.tc.utils

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

inline fun <reified T : Any> provide(): T {
    return object : KoinComponent {
        val value by inject<T>()
    }.value
}