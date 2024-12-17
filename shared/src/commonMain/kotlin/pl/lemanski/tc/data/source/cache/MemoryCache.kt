package pl.lemanski.tc.data.source.cache

/**
 * Key value cache. Used as single source of truth for non-persistent data.
 */
internal class MemoryCache {
    private val keyValueStore: MutableMap<String, Any> = mutableMapOf()

    /**
     * Puts value into cache.
     *
     * @param key The key under which the value will be stored.
     * @param value The value to be stored in the cache.
     * @param T The type of the value.
     */
    fun <T : Any> put(key: String, value: T) {
        keyValueStore[key] = value
    }

    /**
     * Gets value from cache.
     *
     * @param key The key of the value to be retrieved.
     * @param T The type of the value.
     * @return The value associated with the key, or null if the key does not exist.
     */
    inline fun <reified T> get(key: String): T? {
        val value = keyValueStore[key] ?: return null
        return value as T
    }

    /**
     * Removes value from cache.
     *
     * @param key The key of the value to be removed.
     */
    fun remove(key: String) {
        keyValueStore.remove(key)
    }

    /**
     * Clears cache.
     */
    fun clear() {
        keyValueStore.clear()
    }
}