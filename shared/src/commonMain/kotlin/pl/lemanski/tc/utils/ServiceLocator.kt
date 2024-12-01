package pl.lemanski.tc.utils

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

inline fun <reified T : Any> provide(parameters: Any? = null): T {
    return object : KoinComponent {
        val value by inject<T> { parametersOf(parameters) }
    }.value
}