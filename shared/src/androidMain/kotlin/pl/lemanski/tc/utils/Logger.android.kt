package pl.lemanski.tc.utils

import android.util.Log

internal actual fun logMessage(level: LogLevel, tag: String, message: String) {
    when (level) {
        LogLevel.INFO  -> Log.i(tag, message)
        LogLevel.DEBUG -> Log.d(tag, message)
        LogLevel.ERROR -> Log.e(tag, message)
        LogLevel.WARN  -> Log.w(tag, message)
    }
}