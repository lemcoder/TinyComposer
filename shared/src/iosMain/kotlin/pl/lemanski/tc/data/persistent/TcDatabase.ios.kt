package pl.lemanski.tc.data.persistent

actual fun getFilesDirPath(): String {
    val paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true);
    val cacheDirectory = paths.first()
}