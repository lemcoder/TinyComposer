package pl.lemanski.tc.data.persistent

import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual fun getFilesDirPath(): String {
    val paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true);
    val cacheDirectory: String = paths.first() as String

    return cacheDirectory
}