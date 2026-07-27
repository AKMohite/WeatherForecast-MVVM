package app.mak.atmosense

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform