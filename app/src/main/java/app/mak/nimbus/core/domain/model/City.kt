package app.mak.nimbus.core.domain.model

data class City(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String?,
    val state: String?,
    val isDefault: Boolean = false
)