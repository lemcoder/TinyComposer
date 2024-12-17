package pl.lemanski.tc.data.repository.project

import kotlinx.serialization.json.Json
import pl.lemanski.tc.data.model.project.ProjectEntity
import pl.lemanski.tc.data.source.cache.MemoryCache
import pl.lemanski.tc.data.source.persistent.FileSystemDatabase
import pl.lemanski.tc.domain.model.project.Project
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.utils.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProjectRepositoryTest {

    private val database = object : FileSystemDatabase {
        private val files = mutableMapOf<UUID, String>()

        override fun getFiles(): List<UUID> = files.keys.toList()

        override fun loadFile(id: UUID): String = files[id] ?: throw Exception("File not found")

        override fun saveFile(id: UUID, content: String) {
            files[id] = content
        }

        override fun deleteFile(id: UUID) {
            files.remove(id)
        }
    }

    private val memoryCache = MemoryCache()

    private val repository = ProjectRepositoryImpl(database, memoryCache)

    @Test
    fun testGetProjects() {
        val project = Project(UUID.random(), "Test Project", 120, Rhythm.FOUR_FOURS, emptyList(), emptyList())
        repository.saveProject(project)
        val projects = repository.getProjects()
        assertEquals(1, projects.size)
        assertEquals(project.id, projects[0].id)
    }

    @Test
    fun testGetProject() {
        val project = Project(UUID.random(), "Test Project", 120, Rhythm.FOUR_FOURS, emptyList(), emptyList())
        repository.saveProject(project)
        val retrievedProject = repository.getProject(project.id)
        assertEquals(project.id, retrievedProject?.id)
    }

    @Test
    fun testSaveProject() {
        val project = Project(UUID.random(), "Test Project", 120, Rhythm.FOUR_FOURS, emptyList(), emptyList())
        repository.saveProject(project)
        val savedProject = database.loadFile(project.id)
        val projectEntity = Json.decodeFromString<ProjectEntity>(savedProject)
        assertEquals(project.id.toString(), projectEntity.id)
    }

    @Test
    fun testDeleteProject() {
        val project = Project(UUID.random(), "Test Project", 120, Rhythm.FOUR_FOURS, emptyList(), emptyList())
        repository.saveProject(project)
        repository.deleteProject(project.id)
        assertNull(repository.getProject(project.id))
    }

    @Test
    fun testCacheProject() {
        val project = Project(UUID.random(), "Test Project", 120, Rhythm.FOUR_FOURS, emptyList(), emptyList())
        repository.cacheProject(project)
        val cachedProject = repository.getCurrentProject()
        assertEquals(project.id, cachedProject?.id)
    }

    @Test
    fun testGetCurrentProject() {
        val project = Project(UUID.random(), "Test Project", 120, Rhythm.FOUR_FOURS, emptyList(), emptyList())
        repository.cacheProject(project)
        val currentProject = repository.getCurrentProject()
        assertEquals(project.id, currentProject?.id)
    }
}