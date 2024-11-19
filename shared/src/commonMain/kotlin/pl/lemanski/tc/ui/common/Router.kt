package pl.lemanski.tc.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
inline fun <reified T> router(block: (T) -> Unit) where T : TcViewModel<*>, T : ViewModel {
    val viewModel = koinViewModel<T>()

    block(viewModel)

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }
}