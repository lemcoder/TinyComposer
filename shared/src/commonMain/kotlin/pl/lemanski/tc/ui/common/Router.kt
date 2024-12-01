package pl.lemanski.tc.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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

@Composable
internal inline fun <reified T : Destination> NavigationContext(crossinline content: @Composable (key: T) -> Unit) {

}

internal inline fun <reified T : Destination> NavigationService.key(): T? = runBlocking {
    logger.debug("Key: ${T::class.simpleName}")

    withContext(Dispatchers.Default) {
        val history = history()
        val entry: List<T> = history.filterIsInstance<T>()
        if (entry.size > 1) throw NavigationStateException("More than one key of the same type on the stack: \n ${history.map { "- $it\n" }}")
        return@withContext entry.firstOrNull()
    }
}

// FIXME ugly workaround for iOS
@Composable
internal inline fun <reified VM : TcViewModel<*>, reified K : Destination> router(
    viewModel: VM? = null,
    crossinline block: @Composable (VM) -> Unit
) {
    KoinContext {
        if (viewModel == null) {
            // Handle Android
            val navigationService = koinInject<NavigationService>()
            val key = remember { navigationService.key<K>() }

            val vm = koinViewModel<VM>(
                viewModelStoreOwner = key ?: throw NavigationStateException("Key not found in the navigation stack"),
                parameters = { parametersOf(key) }
            )

            block(vm)

            LaunchedEffect(Unit) {
                vm.onAttached()
            }
        } else {
            // Handle iOS
            block(viewModel)
        }
    }
}