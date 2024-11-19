package pl.lemanski.tc.data.persistent

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getFilesDirPath(): String = ContextProvider.context.filesDir.absolutePath

private object ContextProvider : KoinComponent {
    val context: Context by inject()
}