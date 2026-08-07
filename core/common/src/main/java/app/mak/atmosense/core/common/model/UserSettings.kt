package app.mak.atmosense.core.common.model

data class UserSettings(
  val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
  val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.KILOMETERS_PER_HOUR,
  val pressureUnit: PressureUnit = PressureUnit.HECTOPASCAL,
  val distanceUnit: DistanceUnit = DistanceUnit.KILOMETERS,
  val precipitationUnit: PrecipitationUnit = PrecipitationUnit.MILLIMETERS,
  val appTheme: AppTheme = AppTheme.SYSTEM,
  val useDynamicColors: Boolean = true
)
