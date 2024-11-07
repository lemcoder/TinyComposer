package pl.lemanski.tc.data.persistent

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import pl.lemanski.tc.utils.UUID

internal object TcDatabase {
    private val fileDirPath = getFilesDirPath()

    fun getFiles(): List<UUID> {
        val files = SystemFileSystem.list(Path(fileDirPath))
        return files.map { UUID(it.name) }
    }

    fun saveFile(id: UUID, data: String) {
        SystemFileSystem.sink(Path(fileDirPath, "$id.txt")).buffered().writeString(data)
    }

    fun loadFile(id: UUID): String {
        val path = Path(fileDirPath, "$id.txt")

        if (!SystemFileSystem.exists(path)) {
            throw Exception("File not found")
        }

        return SystemFileSystem.source(path).buffered().readString()
    }
}

expect fun getFilesDirPath(): String