package pl.lemanski.tc.data.source.persistent

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal actual fun getFilesDirPath(): String = ContextProvider.context.filesDir.absolutePath

private object ContextProvider : KoinComponent {
    val context: Context by inject()
}