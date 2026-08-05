package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WindDTO(
  @SerialName("deg")
  val degrees: Long? = null,
  @SerialName("gust")
  val gust: Double? = null,
  @SerialName("speed")
  val speed: Double? = null
)
