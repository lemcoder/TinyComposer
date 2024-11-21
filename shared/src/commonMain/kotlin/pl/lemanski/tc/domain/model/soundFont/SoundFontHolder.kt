package pl.lemanski.tc.domain.model.soundFont

data class SoundFontHolder(
    val name: String, // TODO check if can be obtained from the soundFont file
    val soundFont: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundFontHolder

        if (name != other.name) return false
        if (!soundFont.contentEquals(other.soundFont)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + soundFont.contentHashCode()
        return result
    }
}
