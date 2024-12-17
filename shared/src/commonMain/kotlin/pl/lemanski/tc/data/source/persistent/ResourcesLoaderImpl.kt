package pl.lemanski.tc.data.source.persistent

import org.jetbrains.compose.resources.ExperimentalResourceApi
import tinycomposer.shared.generated.resources.Res

internal class ResourcesLoaderImpl : ResourcesLoader {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun loadBytes(path: String): ByteArray {
        return Res.readBytes(path)
    }
}