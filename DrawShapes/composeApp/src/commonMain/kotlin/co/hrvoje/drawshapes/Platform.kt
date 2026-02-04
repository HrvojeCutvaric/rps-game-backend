package co.hrvoje.drawshapes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform