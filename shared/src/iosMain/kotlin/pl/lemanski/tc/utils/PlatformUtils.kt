package pl.lemanski.tc.utils

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create

@OptIn(ExperimentalForeignApi::class)
internal fun getDirUrl(directory: NSSearchPathDirectory, create: Boolean = false): NSURL? {
    memScoped {
        val error = alloc<ObjCObjectVar<NSError?>>()
        return NSFileManager.defaultManager.URLForDirectory(directory, NSUserDomainMask, null, create, error.ptr)?.standardizedURL
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toNSData(): NSData = this.usePinned {
    NSData.create(bytes = it.addressOf(0), length = this.size.convert())
}