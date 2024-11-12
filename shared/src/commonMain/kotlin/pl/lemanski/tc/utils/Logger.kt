package pl.lemanski.tc.utils

import kotlin.reflect.KClass

internal enum class LogLevel {
    INFO,
    DEBUG,
    ERROR,
    WARN
}

internal class Logger(private val clazz: KClass<*>) {
    private val tag = clazz.simpleName ?: "Undefined"

    fun info(message: String) {
        logMessage(LogLevel.INFO, tag, message)
    }

    fun debug(message: String) {
        logMessage(LogLevel.DEBUG, tag, message)
    }

    fun error(message: String, throwable: Throwable? = null) {
        logMessage(LogLevel.ERROR, tag, "$message \n ${throwable?.stackTraceToString()}")
    }

    fun warn(message: String, throwable: Throwable? = null) {
        logMessage(LogLevel.WARN, tag, "$message \n ${throwable?.stackTraceToString()}")
    }
}

internal expect fun logMessage(level: LogLevel, tag: String, message: String)