package app.mak.atmosense.core.data.repository

import androidx.datastore.core.DataStore
import app.mak.atmosense.core.common.model.AppTheme
import app.mak.atmosense.core.common.model.DistanceUnit
import app.mak.atmosense.core.common.model.PrecipitationUnit
import app.mak.atmosense.core.common.model.PressureUnit
import app.mak.atmosense.core.common.model.TemperatureUnit
import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.common.model.WindSpeedUnit
import app.mak.atmosense.core.data.UserSettingsProto
import app.mak.atmosense.core.data.UserSettingsProto.AppThemeProto
import app.mak.atmosense.core.data.UserSettingsProto.DistanceUnitProto
import app.mak.atmosense.core.data.UserSettingsProto.PrecipitationUnitProto
import app.mak.atmosense.core.data.UserSettingsProto.PressureUnitProto
import app.mak.atmosense.core.data.UserSettingsProto.TemperatureUnitProto
import app.mak.atmosense.core.data.UserSettingsProto.WindSpeedUnitProto
import app.mak.atmosense.core.domain.repository.SettingsRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ContributesBinding(scope = AppScope::class)
@Inject
class DefaultSettingsRepository(
  private val userSettingsDataStore: DataStore<UserSettingsProto>
) : SettingsRepository {

  override fun getUserSettings(): Flow<UserSettings> {
    return userSettingsDataStore.data.map { proto ->
      UserSettings(
        temperatureUnit = proto.temperatureUnit.toDomain(),
        windSpeedUnit = proto.windSpeedUnit.toDomain(),
        pressureUnit = proto.pressureUnit.toDomain(),
        distanceUnit = proto.distanceUnit.toDomain(),
        precipitationUnit = proto.precipitationUnit.toDomain(),
        appTheme = proto.appTheme.toDomain(),
        useDynamicColors = proto.useDynamicColors
      )
    }
  }

  override suspend fun updateUserSettings(settings: UserSettings) {
    userSettingsDataStore.updateData { currentProto ->
      currentProto.toBuilder()
        .setTemperatureUnit(settings.temperatureUnit.toProto())
        .setWindSpeedUnit(settings.windSpeedUnit.toProto())
        .setPressureUnit(settings.pressureUnit.toProto())
        .setDistanceUnit(settings.distanceUnit.toProto())
        .setPrecipitationUnit(settings.precipitationUnit.toProto())
        .setAppTheme(settings.appTheme.toProto())
        .setUseDynamicColors(settings.useDynamicColors)
        .build()
    }
  }

  private fun TemperatureUnitProto.toDomain(): TemperatureUnit = when (this) {
    TemperatureUnitProto.TEMPERATURE_UNIT_FAHRENHEIT -> TemperatureUnit.FAHRENHEIT
    TemperatureUnitProto.TEMPERATURE_UNIT_KELVIN -> TemperatureUnit.KELVIN
    else -> TemperatureUnit.CELSIUS
  }

  private fun TemperatureUnit.toProto(): TemperatureUnitProto = when (this) {
    TemperatureUnit.CELSIUS -> TemperatureUnitProto.TEMPERATURE_UNIT_CELSIUS
    TemperatureUnit.FAHRENHEIT -> TemperatureUnitProto.TEMPERATURE_UNIT_FAHRENHEIT
    TemperatureUnit.KELVIN -> TemperatureUnitProto.TEMPERATURE_UNIT_KELVIN
  }

  private fun WindSpeedUnitProto.toDomain(): WindSpeedUnit = when (this) {
    WindSpeedUnitProto.WIND_SPEED_UNIT_MILES_PER_HOUR -> WindSpeedUnit.MILES_PER_HOUR
    WindSpeedUnitProto.WIND_SPEED_UNIT_KNOTS -> WindSpeedUnit.KNOTS
    WindSpeedUnitProto.WIND_SPEED_UNIT_FEET_PER_SECOND -> WindSpeedUnit.FEET_PER_SECOND
    WindSpeedUnitProto.WIND_SPEED_UNIT_METERS_PER_SECOND -> WindSpeedUnit.METERS_PER_SECOND
    WindSpeedUnitProto.WIND_SPEED_UNIT_KILOMETERS_PER_HOUR -> WindSpeedUnit.KILOMETERS_PER_HOUR
    WindSpeedUnitProto.UNRECOGNIZED -> WindSpeedUnit.KILOMETERS_PER_HOUR
  }

  private fun WindSpeedUnit.toProto(): WindSpeedUnitProto = when (this) {
    WindSpeedUnit.METERS_PER_SECOND -> WindSpeedUnitProto.WIND_SPEED_UNIT_METERS_PER_SECOND
    WindSpeedUnit.KILOMETERS_PER_HOUR -> WindSpeedUnitProto.WIND_SPEED_UNIT_KILOMETERS_PER_HOUR
    WindSpeedUnit.MILES_PER_HOUR -> WindSpeedUnitProto.WIND_SPEED_UNIT_MILES_PER_HOUR
    WindSpeedUnit.KNOTS -> WindSpeedUnitProto.WIND_SPEED_UNIT_KNOTS
    WindSpeedUnit.FEET_PER_SECOND -> WindSpeedUnitProto.WIND_SPEED_UNIT_FEET_PER_SECOND
  }

  private fun PressureUnitProto.toDomain(): PressureUnit = when (this) {
    PressureUnitProto.PRESSURE_UNIT_KILOPASCAL -> PressureUnit.KILOPASCAL
    PressureUnitProto.PRESSURE_UNIT_MILLIBAR -> PressureUnit.MILLIBAR
    PressureUnitProto.PRESSURE_UNIT_ATMOSPHERE -> PressureUnit.ATMOSPHERE
    PressureUnitProto.PRESSURE_UNIT_MILLIMETERS_OF_MERCURY -> PressureUnit.MILLIMETERS_OF_MERCURY
    PressureUnitProto.PRESSURE_UNIT_INCHES_OF_MERCURY -> PressureUnit.INCHES_OF_MERCURY
    else -> PressureUnit.HECTOPASCAL
  }

  private fun PressureUnit.toProto(): PressureUnitProto = when (this) {
    PressureUnit.HECTOPASCAL -> PressureUnitProto.PRESSURE_UNIT_HECTOPASCAL
    PressureUnit.KILOPASCAL -> PressureUnitProto.PRESSURE_UNIT_KILOPASCAL
    PressureUnit.MILLIBAR -> PressureUnitProto.PRESSURE_UNIT_MILLIBAR
    PressureUnit.ATMOSPHERE -> PressureUnitProto.PRESSURE_UNIT_ATMOSPHERE
    PressureUnit.MILLIMETERS_OF_MERCURY -> PressureUnitProto.PRESSURE_UNIT_MILLIMETERS_OF_MERCURY
    PressureUnit.INCHES_OF_MERCURY -> PressureUnitProto.PRESSURE_UNIT_INCHES_OF_MERCURY
  }

  private fun DistanceUnitProto.toDomain(): DistanceUnit = when (this) {
    DistanceUnitProto.DISTANCE_UNIT_METERS -> DistanceUnit.METERS
    DistanceUnitProto.DISTANCE_UNIT_MILES -> DistanceUnit.MILES
    DistanceUnitProto.DISTANCE_UNIT_NAUTICAL_MILES -> DistanceUnit.NAUTICAL_MILES
    DistanceUnitProto.DISTANCE_UNIT_FEET -> DistanceUnit.FEET
    else -> DistanceUnit.KILOMETERS
  }

  private fun DistanceUnit.toProto(): DistanceUnitProto = when (this) {
    DistanceUnit.METERS -> DistanceUnitProto.DISTANCE_UNIT_METERS
    DistanceUnit.KILOMETERS -> DistanceUnitProto.DISTANCE_UNIT_KILOMETERS
    DistanceUnit.MILES -> DistanceUnitProto.DISTANCE_UNIT_MILES
    DistanceUnit.NAUTICAL_MILES -> DistanceUnitProto.DISTANCE_UNIT_NAUTICAL_MILES
    DistanceUnit.FEET -> DistanceUnitProto.DISTANCE_UNIT_FEET
  }

  private fun PrecipitationUnitProto.toDomain(): PrecipitationUnit = when (this) {
    PrecipitationUnitProto.PRECIPITATION_UNIT_CENTIMETERS -> PrecipitationUnit.CENTIMETERS
    PrecipitationUnitProto.PRECIPITATION_UNIT_INCHES -> PrecipitationUnit.INCHES
    PrecipitationUnitProto.PRECIPITATION_UNIT_LITERS_PER_SQUARE_METER -> PrecipitationUnit.LITERS_PER_SQUARE_METER
    else -> PrecipitationUnit.MILLIMETERS
  }

  private fun PrecipitationUnit.toProto(): PrecipitationUnitProto = when (this) {
    PrecipitationUnit.MILLIMETERS -> PrecipitationUnitProto.PRECIPITATION_UNIT_MILLIMETERS
    PrecipitationUnit.CENTIMETERS -> PrecipitationUnitProto.PRECIPITATION_UNIT_CENTIMETERS
    PrecipitationUnit.INCHES -> PrecipitationUnitProto.PRECIPITATION_UNIT_INCHES
    PrecipitationUnit.LITERS_PER_SQUARE_METER -> PrecipitationUnitProto.PRECIPITATION_UNIT_LITERS_PER_SQUARE_METER
  }

  private fun AppThemeProto.toDomain(): AppTheme = when (this) {
    AppThemeProto.APP_THEME_LIGHT -> AppTheme.LIGHT
    AppThemeProto.APP_THEME_DARK -> AppTheme.DARK
    else -> AppTheme.SYSTEM
  }

  private fun AppTheme.toProto(): AppThemeProto = when (this) {
    AppTheme.SYSTEM -> AppThemeProto.APP_THEME_SYSTEM
    AppTheme.LIGHT -> AppThemeProto.APP_THEME_LIGHT
    AppTheme.DARK -> AppThemeProto.APP_THEME_DARK
  }
}
