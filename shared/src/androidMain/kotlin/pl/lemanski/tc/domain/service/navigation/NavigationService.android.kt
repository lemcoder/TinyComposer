package pl.lemanski.tc.domain.service.navigation

import kotlinx.coroutines.withContext
import pl.lemanski.tc.domain.model.navigation.NavigationEvent

actual suspend fun NavigationService.back(): Boolean = withContext(dispatcher) {
    logger.debug("Back")
    var result = true

    updateHistory { history ->
        if (history.size <= 1) {
            logger.debug("Back: No more destinations")
            result = false
            return@updateHistory history
        }

        val newHistory = history.toMutableList()
        val removed = newHistory.removeAt(newHistory.size - 1)
        removed.viewModelStore.clear()
        newHistory.toSet()
    }

    if (!result) {
        return@withContext result
    }

    val newHistory = history()

    getOnNavigateListener()?.onNavigate(
        NavigationEvent(
            destination = newHistory.last(),
            direction = NavigationEvent.Direction.BACKWARD
        )
    )

    return@withContext result
}