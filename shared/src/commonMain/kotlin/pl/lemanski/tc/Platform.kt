package pl.lemanski.tc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform