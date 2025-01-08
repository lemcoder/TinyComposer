package pl.lemanski.tc.data.source.remote

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

internal fun jelly(): String {
    val x = 10.toString(16).uppercase() + "Iz" + 10.toString(16).lowercase() + Char(83) + "y" + 181.toString(16).uppercase()
    val y = Char(80) + "Ou" + sin(PI/2).roundToInt().toString() + "oh" + Char(85) + Char(360/4) + 139.toString(16).lowercase() + "D"
    return x + y
}

@OptIn(ExperimentalEncodingApi::class)
internal fun fish(): String {
    return Base64.Default.decode("allnRlZnd0I4MnpkM2k4MHRDbVE=".encodeToByteArray()).decodeToString()
}