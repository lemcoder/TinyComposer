package pl.lemanski.tc.utils

import android.util.Log

internal actual fun logMessage(level: LogLevel, tag: String, message: String) {
    when (level) {
        LogLevel.INFO  -> Log.i(tag, "I/$message")
        LogLevel.DEBUG -> Log.d(tag, "D/$message")
        LogLevel.ERROR -> Log.e(tag, "E/$message")
        LogLevel.WARN  -> Log.w(tag, "W/$message")
    }
}