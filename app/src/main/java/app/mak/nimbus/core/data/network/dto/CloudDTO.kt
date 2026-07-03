package app.mak.nimbus.core.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CloudDTO(
    @SerialName("all")
    val all: Int? = null
)