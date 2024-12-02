package pl.lemanski.tc.ui.common

import androidx.compose.runtime.Composable
import pl.lemanski.tc.domain.model.navigation.Destination

@Composable
internal actual inline fun <reified VM : TcViewModel<*>, reified K : Destination> router(
    crossinline block: @Composable () -> Unit
) {
    // on iOS we provide viewModel higher in the hierarchy
    block()
}