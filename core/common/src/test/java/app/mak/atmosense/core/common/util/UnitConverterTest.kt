package app.mak.atmosense.core.common.util

import app.mak.atmosense.core.common.model.TemperatureUnit
import app.mak.atmosense.core.common.model.WindSpeedUnit
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitConverterTest {

  @Test
  fun `convert temperature from Kelvin`() {
    val kelvin = 300.0
    assertEquals(26.85, UnitConverter.convertTemperature(kelvin, TemperatureUnit.CELSIUS), 0.01)
    assertEquals(80.33, UnitConverter.convertTemperature(kelvin, TemperatureUnit.FAHRENHEIT), 0.01)
    assertEquals(300.0, UnitConverter.convertTemperature(kelvin, TemperatureUnit.KELVIN), 0.01)
  }

  @Test
  fun `convert wind speed from meters per second`() {
    val ms = 10.0
    assertEquals(36.0, UnitConverter.convertWindSpeed(ms, WindSpeedUnit.KILOMETERS_PER_HOUR), 0.01)
    assertEquals(22.37, UnitConverter.convertWindSpeed(ms, WindSpeedUnit.MILES_PER_HOUR), 0.01)
  }
}
