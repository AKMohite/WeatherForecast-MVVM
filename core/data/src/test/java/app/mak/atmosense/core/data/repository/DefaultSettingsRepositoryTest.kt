package app.mak.atmosense.core.data.repository

import app.mak.atmosense.core.common.model.AppTheme
import app.mak.atmosense.core.common.model.DistanceUnit
import app.mak.atmosense.core.common.model.PrecipitationUnit
import app.mak.atmosense.core.common.model.PressureUnit
import app.mak.atmosense.core.common.model.TemperatureUnit
import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.common.model.WindSpeedUnit
import app.mak.atmosense.core.data.UserSettingsProto
import app.mak.atmosense.core.data.testing.FakeDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultSettingsRepositoryTest {

  private lateinit var userSettingsDataStore: FakeDataStore<UserSettingsProto>
  private lateinit var repository: DefaultSettingsRepository

  @Before
  fun setUp() {
    userSettingsDataStore = FakeDataStore(UserSettingsProto.getDefaultInstance())
    repository = DefaultSettingsRepository(userSettingsDataStore)
  }

  @Test
  fun `getUserSettings returns default settings when datastore is empty`() = runTest {
    val settings = repository.getUserSettings().first()

    assertEquals(TemperatureUnit.CELSIUS, settings.temperatureUnit)
    assertEquals(WindSpeedUnit.METERS_PER_SECOND, settings.windSpeedUnit)
    assertEquals(PressureUnit.HECTOPASCAL, settings.pressureUnit)
    assertEquals(DistanceUnit.METERS, settings.distanceUnit)
    assertEquals(PrecipitationUnit.MILLIMETERS, settings.precipitationUnit)
    assertEquals(AppTheme.SYSTEM, settings.appTheme)
    assertEquals(false, settings.useDynamicColors)
  }

  @Test
  fun `getUserSettings maps proto fields correctly`() = runTest {
    userSettingsDataStore.updateData {
      it.toBuilder()
        .setTemperatureUnit(UserSettingsProto.TemperatureUnitProto.TEMPERATURE_UNIT_FAHRENHEIT)
        .setWindSpeedUnit(UserSettingsProto.WindSpeedUnitProto.WIND_SPEED_UNIT_MILES_PER_HOUR)
        .setPressureUnit(UserSettingsProto.PressureUnitProto.PRESSURE_UNIT_MILLIBAR)
        .setDistanceUnit(UserSettingsProto.DistanceUnitProto.DISTANCE_UNIT_MILES)
        .setPrecipitationUnit(UserSettingsProto.PrecipitationUnitProto.PRECIPITATION_UNIT_INCHES)
        .setAppTheme(UserSettingsProto.AppThemeProto.APP_THEME_DARK)
        .setUseDynamicColors(false)
        .build()
    }

    val settings = repository.getUserSettings().first()

    assertEquals(TemperatureUnit.FAHRENHEIT, settings.temperatureUnit)
    assertEquals(WindSpeedUnit.MILES_PER_HOUR, settings.windSpeedUnit)
    assertEquals(PressureUnit.MILLIBAR, settings.pressureUnit)
    assertEquals(DistanceUnit.MILES, settings.distanceUnit)
    assertEquals(PrecipitationUnit.INCHES, settings.precipitationUnit)
    assertEquals(AppTheme.DARK, settings.appTheme)
    assertEquals(false, settings.useDynamicColors)
  }

  @Test
  fun `updateUserSettings updates datastore correctly`() = runTest {
    val newSettings = UserSettings(
      temperatureUnit = TemperatureUnit.KELVIN,
      windSpeedUnit = WindSpeedUnit.KNOTS,
      pressureUnit = PressureUnit.ATMOSPHERE,
      distanceUnit = DistanceUnit.NAUTICAL_MILES,
      precipitationUnit = PrecipitationUnit.CENTIMETERS,
      appTheme = AppTheme.LIGHT,
      useDynamicColors = true
    )

    repository.updateUserSettings(newSettings)

    val proto = userSettingsDataStore.data.first()
    assertEquals(
      UserSettingsProto.TemperatureUnitProto.TEMPERATURE_UNIT_KELVIN,
      proto.temperatureUnit
    )
    assertEquals(UserSettingsProto.WindSpeedUnitProto.WIND_SPEED_UNIT_KNOTS, proto.windSpeedUnit)
    assertEquals(UserSettingsProto.PressureUnitProto.PRESSURE_UNIT_ATMOSPHERE, proto.pressureUnit)
    assertEquals(
      UserSettingsProto.DistanceUnitProto.DISTANCE_UNIT_NAUTICAL_MILES,
      proto.distanceUnit
    )
    assertEquals(
      UserSettingsProto.PrecipitationUnitProto.PRECIPITATION_UNIT_CENTIMETERS,
      proto.precipitationUnit
    )
    assertEquals(UserSettingsProto.AppThemeProto.APP_THEME_LIGHT, proto.appTheme)
    assertEquals(true, proto.useDynamicColors)
  }

  @Test
  fun `mapping handles all unit types correctly`() = runTest {
    // This test ensures that even more variants are mapped correctly
    val settingsToTest = UserSettings(
      temperatureUnit = TemperatureUnit.CELSIUS,
      windSpeedUnit = WindSpeedUnit.METERS_PER_SECOND,
      pressureUnit = PressureUnit.KILOPASCAL,
      distanceUnit = DistanceUnit.METERS,
      precipitationUnit = PrecipitationUnit.LITERS_PER_SQUARE_METER,
      appTheme = AppTheme.SYSTEM,
      useDynamicColors = false
    )

    repository.updateUserSettings(settingsToTest)
    val retrievedSettings = repository.getUserSettings().first()

    assertEquals(settingsToTest, retrievedSettings)
  }
}
