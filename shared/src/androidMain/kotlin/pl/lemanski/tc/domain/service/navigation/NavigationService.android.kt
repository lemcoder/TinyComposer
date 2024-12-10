package pl.lemanski.tc.domain.service.navigation

import pl.lemanski.tc.domain.model.navigation.NavigationEvent

actual fun NavigationService.back(): Boolean {
    logger.debug("Back")
    var result = true

    updateHistory { history ->
        if (history.size <= 1) {
            logger.debug("Back: No more destinations")
            result = false
            return@updateHistory history
        }

        val newHistory = history.toMutableList()
        val removed = newHistory.removeAt(newHistory.lastIndex)
        removed.viewModelStore.clear()

        newHistory.toSet()
    }

    if (!result) {
        return false
    }

    val newHistory = history()

    getOnNavigateListener()?.onNavigate(
        NavigationEvent(
            destination = newHistory.last(),
            direction = NavigationEvent.Direction.BACKWARD
        )
    )

    return result
}