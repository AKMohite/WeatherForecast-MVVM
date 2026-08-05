package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SysDTO(
  @SerialName("sunrise")
  val sunrise: Int? = null,
  @SerialName("sunset")
  val sunset: Int? = null,
  @SerialName("country")
  val country: String? = null,
  @SerialName("pod")
  val pod: String? = null
)
