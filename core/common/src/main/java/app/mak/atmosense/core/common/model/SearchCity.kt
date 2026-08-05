package app.mak.atmosense.core.common.model

data class SearchCity(
  val country: String,
  val latitude: Double,
  val longitude: Double,
  val name: String,
  val state: String
) {
  fun toCoordinates(): LocationCoordinate = LocationCoordinate(latitude, longitude)

  val id = "$country-$latitude-$longitude"
  val subtitle = "$state, $country"
}
