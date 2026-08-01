package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDTO(
  @SerialName("description")
  val description: String? = null,
  @SerialName("icon")
  val iconCode: String? = null,
  @SerialName("id")
  val id: Long? = null,
  @SerialName("main")
  val main: String? = null
)
