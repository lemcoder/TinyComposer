package pl.lemanski.tc.domain.service.navigation

import kotlinx.coroutines.test.runTest
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.NavigationEvent
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
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
    private lateinit var navigationService: NavigationService

    private var mockListener: OnNavigateListener = object : OnNavigateListener {
        override fun onNavigate(event: NavigationEvent) {
            callCounter++
        }
    }

    @BeforeTest
    fun setup() = runTest {
        callCounter = 0
        navigationService = NavigationService()
        navigationService.setOnNavigateListener(mockListener)
    }

    @Test
    fun `goTo should add destination to stack and notify listener`() = runTest {
        val destination = WelcomeDestination
        navigationService.goTo(destination)

        assertEquals(1, navigationService.history().size)
        assertEquals(destination, navigationService.history().last())
        assertEquals(1, callCounter)
    }

    @Test
    fun `back should remove last destination and notify listener`() = runTest {
        val startDestination = WelcomeDestination
        val projectsDestination = ProjectListDestination
        navigationService.goTo(startDestination)
        navigationService.goTo(projectsDestination)

        val result = navigationService.back()

        assertTrue(result)
        assertEquals(1, navigationService.history().size)
        assertEquals(startDestination, navigationService.history().last())
        assertEquals(3, callCounter)
    }

    @Test
    fun `back should return false if only one destination in stack`() = runTest {
        val destination = WelcomeDestination
        navigationService.goTo(destination)

        val result = navigationService.back()

        assertFalse { result }
        assertEquals(1, navigationService.history().size)
        assertEquals(1, callCounter)
    }

    @Test
    fun `key should return destination of the correct type`() = runTest {
        val homeDestination = WelcomeDestination
        navigationService.goTo(homeDestination)

        val result: Destination? = navigationService.key<Destination>()
        assertEquals(homeDestination, result)
    }

    @Test
    fun `key should return null if no destination of type exists`() = runTest {
        val result: Destination? = navigationService.key<Destination>()
        assertNull(result)
    }

    @Test
    fun `key should throw exception if more than one destination of type exists`() = runTest {
        val homeDestination = WelcomeDestination
        val projectsDestination = ProjectListDestination
        navigationService.goTo(homeDestination)
        navigationService.goTo(projectsDestination)
        navigationService.goTo(homeDestination)

        val exception = assertFailsWith<NavigationStateException> {
            navigationService.key<Destination>()
        }

        assertTrue(exception.message.contains("More than one key of the same type on the stack"))
    }
}