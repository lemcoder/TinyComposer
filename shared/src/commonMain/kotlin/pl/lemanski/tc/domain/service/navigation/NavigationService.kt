package pl.lemanski.tc.domain.service.navigation

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.NavigationEvent
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.utils.Logger

/**
 * Service that is responsible for navigation between [Destination]s
 * This implementation is based on single callback so it is easy to apply to platform navigation
 * on each supported platform
 */
class NavigationService {
    // TODO logger should be injected (maybe use context receiver here?)
    internal val logger = Logger(this::class)

    private val listenerMut: Mutex = Mutex()
    private var listener: OnNavigateListener? = null

    private val historyMut: Mutex = Mutex()
    private var history: Set<Destination> = setOf(WelcomeDestination)

    fun setOnNavigateListener(listener: OnNavigateListener?) = runBlocking {
        logger.debug("Set on navigate listener")

        listenerMut.withLock {
            this@NavigationService.listener = listener
        }
    }

    internal fun getOnNavigateListener(): OnNavigateListener? = runBlocking {
        logger.info("Get on navigate listener")

        return@runBlocking listenerMut.withLock {
            listener
        }
    }

    internal suspend fun updateHistory(reducer: (Set<Destination>) -> Set<Destination>) {
        val newHistory = reducer(history)
        logger.debug("Update history with:\n${newHistory.joinToString(separator = "\n") { it.toString() }}")

        historyMut.withLock {
            history = newHistory
        }
    }

    internal suspend fun history(): Set<Destination> {
        logger.info("History")

        return historyMut.withLock {
            history
        }
    }
}

fun NavigationService.goTo(destination: Destination) = runBlocking {
    logger.debug("Go to: $destination")

    updateHistory { history ->
        val newHistory = history.toMutableList()
        newHistory.add(destination)
        newHistory.toSet()
    }

    getOnNavigateListener()?.onNavigate(
        NavigationEvent(
            destination = history().last(),
            direction = NavigationEvent.Direction.FORWARD
        )
    )
}

fun NavigationService.back(): Boolean = runBlocking {
    logger.debug("Back")

    updateHistory { history ->
        if (history.size <= 1) {
            logger.debug("Back: No more destinations")
            return@updateHistory history
        }

        val newHistory = history.toMutableList()
        val removed = newHistory.removeAt(newHistory.size - 1)
        removed.viewModelStore.clear()
        newHistory.toSet()
    }

    val newHistory = history()

    getOnNavigateListener()?.onNavigate(
        NavigationEvent(
            destination = newHistory.last(),
            direction = NavigationEvent.Direction.BACKWARD
        )
    )

    return@runBlocking true
}