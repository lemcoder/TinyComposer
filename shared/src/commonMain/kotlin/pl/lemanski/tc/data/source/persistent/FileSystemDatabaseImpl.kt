package pl.lemanski.tc.data.source.persistent

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.exception.EntryNotFoundException

internal class FileSystemDatabaseImpl : FileSystemDatabase {
    private val name: String = "db"
    private val logger = Logger(this::class)
    private val dbPath = Path(getFilesDirPath(), name)

    init {
        if (!SystemFileSystem.exists(dbPath)) {
            SystemFileSystem.createDirectories(dbPath)
        }
    }

    override fun getFiles(): List<UUID> {
        val files = SystemFileSystem.list(dbPath)
        return files.map { UUID(it.name) }
    }

    override fun saveFile(id: UUID, data: String) {
        logger.debug("Save file: $id: $data")

        val sink = SystemFileSystem.sink(Path(dbPath, "$id")).buffered()
        sink.writeString(data)
        sink.close()
    }

    override fun loadFile(id: UUID): String {
        logger.debug("Load file: $id")
        val path = Path(dbPath, "$id")

        if (!SystemFileSystem.exists(path)) {
            throw EntryNotFoundException("File not found")
        }

        return SystemFileSystem.source(path).buffered().readString()
    }

    override fun deleteFile(id: UUID) {
        logger.debug("Delete file: $id")
        val path = Path(dbPath, "$id")

        if (!SystemFileSystem.exists(path)) {
            throw EntryNotFoundException("File not found")
        }

        SystemFileSystem.delete(path)
    }
}

/**
 *  Returns path to the directory where persistent files are stored.
 */
internal expect fun getFilesDirPath(): String