package pl.lemanski.tc.domain.service.navigation

import pl.lemanski.tc.domain.model.navigation.NavigationEvent

/**
 * Used to navigate back to the previous destination
 * Without notifying the listener. This is useful when we want to sync navigation state
 * on iOS when we are using native navigation. This is a workaround for the lack of
 * navigation event callback (like BackHandler) on iOS. We can only react to navigation already
 * done by the user.
 */
internal fun NavigationService.silentBack(): Boolean {
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
        logger.debug("Back: clearing viewModelStore for $removed")
        removed.viewModelStore.clear()
        newHistory.toSet()
    }

    return result
}

/**
 * Stub for back navigation on iOS
 * We only notify the listener about the navigation event
 * The synchronization of the navigation state is done in the [silentBack] method
 * after UINavigationController viewWillDisappear event
 */
actual fun NavigationService.back(): Boolean {
    logger.debug("Back")
    val history = history()

    if (history.size <= 1) {
        logger.debug("Back: No more destinations")
        return false
    }

    val newHistory = history()

    getOnNavigateListener()?.onNavigate(
        NavigationEvent(
            destination = newHistory.last(),
            direction = NavigationEvent.Direction.BACKWARD
        )
    )

    return true
}