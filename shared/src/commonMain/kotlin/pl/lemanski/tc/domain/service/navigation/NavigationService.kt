package pl.lemanski.tc.domain.service.navigation

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.NavigationEvent
import pl.lemanski.tc.domain.model.navigation.ProjectListDestination
import pl.lemanski.tc.utils.Logger

/**
 * Service that is responsible for navigation between [Destination]s
 * This implementation is based on single callback so it is easy to apply to platform navigation
 * on each supported platform
 */
class NavigationService {
    internal val logger = Logger(this::class)
    private var listener: OnNavigateListener? = null
    private var history: Set<Destination> = setOf(ProjectListDestination)

    fun setOnNavigateListener(listener: OnNavigateListener?) = runBlocking {
        logger.debug("Set on navigate listener")
        this@NavigationService.listener = listener
    }

    internal fun getOnNavigateListener(): OnNavigateListener? {
        logger.info("Get on navigate listener")
        return listener
    }

    internal fun updateHistory(reducer: (Set<Destination>) -> Set<Destination>) {
        val newHistory = reducer(history)
        logger.debug("Update history with:\n${newHistory.joinToString(separator = "\n") { it.toString() }}")

        history = newHistory
    }

    internal fun history(): Set<Destination> {
        logger.info("History")
        return history.toSet() // Return copy of history
    }
}

fun NavigationService.goTo(destination: Destination) {
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

expect fun NavigationService.back(): Boolean