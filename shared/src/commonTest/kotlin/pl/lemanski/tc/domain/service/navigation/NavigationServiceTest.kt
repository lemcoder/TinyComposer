package pl.lemanski.tc.domain.service.navigation

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.ProjectDetailsDestination
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.ui.common.key
import pl.lemanski.tc.utils.UUID
import pl.lemanski.tc.utils.exception.NavigationStateException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NavigationServiceTest {
    private var callCounter: Int = 0
    private var mockListener: OnNavigateListener = OnNavigateListener { callCounter++ }

    private val TestScope.navigationService: NavigationService
        get() = NavigationService().apply {
            setOnNavigateListener(mockListener)
        }

    @BeforeTest
    fun setup() = runTest {
        callCounter = 0
    }

    @Test
    fun `goTo should add destination to stack and notify listener`() = runTest {
        val service = navigationService
        val destination = ProjectListDestination
        service.goTo(destination)

        assertEquals(1, service.history().size)
        assertEquals(destination, service.history().last())
        assertEquals(1, callCounter)
    }

    @Test
    fun `back should remove last destination and notify listener`() = runTest {
        val service = navigationService
        val startDestination = ProjectListDestination
        val projectsDestination = ProjectDetailsDestination(UUID.random())
        service.goTo(startDestination)
        service.goTo(projectsDestination)

        val result = service.back()

        assertTrue(result)
        assertEquals(1, service.history().size)
        assertEquals(startDestination, service.history().last())
        assertEquals(3, callCounter)
    }

    @Test
    fun `back should return false if only one destination in stack`() = runTest {
        val service = navigationService
        val destination = ProjectListDestination
        service.goTo(destination)

        val result = service.back()

        assertFalse { result }
        assertEquals(1, service.history().size)
        assertEquals(1, callCounter)
    }

    @Test
    fun `key should return destination of the correct type`() = runTest {
        val service = navigationService

        val homeDestination = ProjectListDestination
        service.goTo(homeDestination)

        val result: Destination? = service.key<Destination>()
        assertEquals(homeDestination, result)
    }

    @Test
    fun `key should return null if no destination of type exists`() = runTest {
        val result: Destination? = navigationService.key<ProjectDetailsDestination>()
        assertNull(result)
    }

    @Test
    fun `key should throw exception if more than one destination of type exists`() = runTest {
        val service = navigationService
        val homeDestination = ProjectListDestination
        val projectsDestination = ProjectDetailsDestination(UUID.random())
        service.goTo(homeDestination)
        service.goTo(projectsDestination)
        service.goTo(homeDestination)

        val exception = assertFailsWith<NavigationStateException> {
            service.key<Destination>()
        }

        assertTrue(exception.message.contains("More than one key of the same type on the stack"))
    }
}