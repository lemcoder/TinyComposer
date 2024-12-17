package pl.lemanski.tc.data.source.persistent

import pl.lemanski.tc.utils.UUID

/**
 * Interface for interacting with the file system database.
 */
internal interface FileSystemDatabase {
    /**
     * Retrieves a list of all file IDs in the database.
     *
     * @return List of UUIDs representing the file IDs.
     */
    fun getFiles(): List<UUID>

    /**
     * Saves a file with the given ID and data to the database.
     *
     * @param id The UUID of the file to be saved.
     * @param data The content to be saved in the file.
     */
    fun saveFile(id: UUID, data: String)

    /**
     * Loads the content of a file with the given ID from the database.
     *
     * @param id The UUID of the file to be loaded.
     * @return The content of the file as a String.
     * @throws EntryNotFoundException if the file is not found.
     */
    fun loadFile(id: UUID): String

    /**
     * Deletes a file with the given ID from the database.
     *
     * @param id The UUID of the file to be deleted.
     * @throws EntryNotFoundException if the file is not found.
     */
    fun deleteFile(id: UUID)
}
