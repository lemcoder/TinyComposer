package pl.lemanski.tc.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.utils.exception.NavigationStateException

internal inline fun <reified T : Destination> NavigationService.key(): T? = runBlocking {
    logger.debug("Key: ${T::class.simpleName}")

    withContext(Dispatchers.Default) {
        val history = history()
        val entry: List<T> = history.filterIsInstance<T>()
        if (entry.size > 1) throw NavigationStateException("More than one key of the same type on the stack: \n ${history.map { "- $it\n" }}")
        return@withContext entry.firstOrNull()
    }
}

val localViewModel = staticCompositionLocalOf<TcViewModel<*>> {
    error("No ViewModel provided")
}

@Composable
internal expect inline fun <reified VM : TcViewModel<*>, reified K : Destination> router(
    crossinline block: @Composable () -> Unit
)

