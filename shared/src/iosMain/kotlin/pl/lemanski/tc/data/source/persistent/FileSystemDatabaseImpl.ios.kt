package pl.lemanski.tc.data.source.persistent

import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

/**
 *  Returns path to the directory where persistent files are stored.
 */
actual fun getFilesDirPath(): String {
    val paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true);
    val cacheDirectory: String = paths.first() as String

    return cacheDirectory
}