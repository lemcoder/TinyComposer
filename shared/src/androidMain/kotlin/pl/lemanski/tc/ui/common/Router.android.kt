package pl.lemanski.tc.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.utils.exception.NavigationStateException

@Composable
internal actual inline fun <reified VM : TcViewModel<*>, reified K : Destination> router(
    crossinline block: @Composable () -> Unit
) {
    KoinContext {
        val navigationService = koinInject<NavigationService>()
        val key = remember { navigationService.key<K>() }

        val vm = koinViewModel<VM>(
            viewModelStoreOwner = key
                ?: throw NavigationStateException("Key not found in the navigation stack"),
            parameters = { parametersOf(key) }
        )

        CompositionLocalProvider(
            localViewModel provides vm
        ) {
            block()
        }

        LaunchedEffect(Unit) {
            vm.onAttached()
        }
    }
}