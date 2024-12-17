package pl.lemanski.tc.data.repository.project

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.lemanski.tc.data.model.project.ProjectEntity
import pl.lemanski.tc.data.model.project.toDomain
import pl.lemanski.tc.data.model.project.toEntity
import pl.lemanski.tc.data.source.cache.MemoryCache
import pl.lemanski.tc.data.source.persistent.FileSystemDatabase
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.repository.project.ProjectRepository
import pl.lemanski.tc.utils.Logger
import pl.lemanski.tc.utils.UUID

internal class ProjectRepositoryImpl(
    private val database: FileSystemDatabase,
    private val cache: MemoryCache
) : ProjectRepository {
    private val logger = Logger(this::class)

    override fun getProjects(): List<Project> = database.getFiles().mapNotNull { getProject(it) }

    override fun getProject(id: UUID): Project? {
        try {
            val projectString = database.loadFile(id)
            val project = Json.decodeFromString<ProjectEntity>(projectString).toDomain()
            return project
        } catch (ex: Exception) {
            logger.error("Failed to load project: $id", ex)
        }

        return null
    }

    override fun saveProject(project: Project): Project {
        database.saveFile(project.id, Json.encodeToString(project.toEntity()))
        return project
    }

    override fun deleteProject(id: UUID): Project? {
        try {
            val project = getProject(id)

            if (project == null) {
                logger.error("Project not found: $id")
                return null
            }

            database.deleteFile(id)

            return project
        } catch (ex: Exception) {
            logger.error("Failed to delete project: $id", ex)
        }

        return null
    }

    //---

    override fun cacheProject(project: Project): Project? {
        cache.put(CURRENT_PROJECT_KEY, project)

        return cache.get(CURRENT_PROJECT_KEY)
    }

    override fun getCurrentProject(): Project? {
        return cache.get(CURRENT_PROJECT_KEY)
    }

    //---

    companion object {
        private const val CURRENT_PROJECT_KEY = "current_project"
    }
}