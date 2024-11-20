package pl.lemanski.tc.data.persistent

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

internal interface TcDatabase {
    fun getFiles(): List<UUID>
    fun saveFile(id: UUID, data: String)
    fun loadFile(id: UUID): String
}

internal class TcDatabaseImpl : TcDatabase {
    private val name: String = "tc"
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
            throw Exception("File not found")
        }

        return SystemFileSystem.source(path).buffered().readString()
    }
}

expect fun getFilesDirPath(): String