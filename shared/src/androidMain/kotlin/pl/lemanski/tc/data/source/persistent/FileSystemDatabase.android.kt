package pl.lemanski.tc.data.source.persistent

import pl.lemanski.tc.ContextProvider

internal actual fun getFilesDirPath(): String = ContextProvider.context.filesDir.absolutePath

