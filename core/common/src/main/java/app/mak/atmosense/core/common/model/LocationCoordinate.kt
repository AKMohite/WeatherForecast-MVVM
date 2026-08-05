package app.mak.atmosense.core.common.model

data class LocationCoordinate(
  val latitude: Double,
  val longitude: Double,
  val isCurrentLocation: Boolean = false
)
