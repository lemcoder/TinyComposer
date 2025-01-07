package pl.lemanski.tc.domain.model.audio

import pl.lemanski.tc.utils.Logger

internal data class AudioStream(
    val data: FloatArray,
    val output: Output,
) {
    enum class Output(val value: Int) {
        MONO(1),
        // STEREO(2) // TODO enable stereo output
    }

    private val logger = Logger(AudioStream::class)
    private val _markers = mutableListOf<AudioMarker>()

    val markers: List<AudioMarker>
        get() = _markers

    var markerReachedCallback: ((AudioMarker) -> Unit)? = null
        private set

    val duration = (data.size * Float.SIZE_BYTES) / SAMPLE_RATE

    val sizeInBytes = data.size * Float.SIZE_BYTES

    /**
     * Add a marker at the given position.
     */
    fun addMarker(position: Int) {
        if (position < 0 || position > data.size * Float.SIZE_BYTES) {
            throw IllegalArgumentException("Invalid marker position")
        }

        val index = _markers.size
        logger.error("Adding marker at position: $position, index: $index, data size: ${data.size}")
        _markers.add(
            AudioMarker(
                position = position,
                index = index
            )
        )
    }

    /**
     * Add a callback that will be called when a marker is reached.
     * @param callback The callback that will be called when a marker is reached.
     */
    fun onMarkerReached(callback: (AudioMarker) -> Unit) {
        logger.info("Setting marker reached callback")
        markerReachedCallback = callback
    }

    //---

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + SAMPLE_RATE
        result = 31 * result + output.value
        result = 31 * result + duration.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AudioStream

        if (!data.contentEquals(other.data)) return false
        if (output != other.output) return false
        if (logger != other.logger) return false
        if (_markers != other._markers) return false
        if (markerReachedCallback != other.markerReachedCallback) return false
        if (duration != other.duration) return false
        if (sizeInBytes != other.sizeInBytes) return false

        return true
    }

    companion object {
        const val SAMPLE_RATE = 44_100

        val END_MARKER = AudioMarker(
            position = -1,
            index = -1
        )

        val EMPTY = AudioStream(floatArrayOf(), Output.MONO)
    }
}

internal expect fun AudioStream.play(isLoopingEnabled: Boolean = false, tempo: Int)

internal expect fun AudioStream.stop()