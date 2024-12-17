package pl.lemanski.tc.data.source.persistent

/**
 * Interface for loading resources from a given path.
 */
internal interface ResourcesLoader {
    /**
     * Loads the content of a resource as a byte array.
     *
     * @param path The path to the resource.
     * @return The content of the resource as a [ByteArray].
     */
    suspend fun loadBytes(path: String): ByteArray
}
