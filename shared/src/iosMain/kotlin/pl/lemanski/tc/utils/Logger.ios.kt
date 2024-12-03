package pl.lemanski.tc.utils

import platform.Foundation.NSLog

internal actual fun logMessage(level: LogLevel, tag: String, message: String) {
    NSLog("[${level.name}] $tag: $message")
}