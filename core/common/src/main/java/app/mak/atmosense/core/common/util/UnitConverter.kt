package app.mak.atmosense.core.common.util

import app.mak.atmosense.core.common.model.DistanceUnit
import app.mak.atmosense.core.common.model.PrecipitationUnit
import app.mak.atmosense.core.common.model.PressureUnit
import app.mak.atmosense.core.common.model.TemperatureUnit
import app.mak.atmosense.core.common.model.WindSpeedUnit

object UnitConverter {

  /**
   * Converts temperature from Kelvin to the specified unit.
   */
  fun convertTemperature(value: Double, toUnit: TemperatureUnit): Double {
    return when (toUnit) {
      TemperatureUnit.KELVIN -> value
      TemperatureUnit.CELSIUS -> value - 273.15
      TemperatureUnit.FAHRENHEIT -> (value - 273.15) * 9 / 5 + 32
    }
  }

  /**
   * Converts wind speed from meters/second to the specified unit.
   */
  fun convertWindSpeed(value: Double, toUnit: WindSpeedUnit): Double {
    return when (toUnit) {
      WindSpeedUnit.METERS_PER_SECOND -> value
      WindSpeedUnit.KILOMETERS_PER_HOUR -> value * 3.6
      WindSpeedUnit.MILES_PER_HOUR -> value * 2.23694
      WindSpeedUnit.KNOTS -> value * 1.94384
      WindSpeedUnit.FEET_PER_SECOND -> value * 3.28084
    }
  }

  /**
   * Converts pressure from hPa to the specified unit.
   */
  fun convertPressure(value: Long, toUnit: PressureUnit): Double {
    val doubleValue = value.toDouble()
    return when (toUnit) {
      PressureUnit.HECTOPASCAL, PressureUnit.MILLIBAR -> doubleValue
      PressureUnit.KILOPASCAL -> doubleValue / 10.0
      PressureUnit.ATMOSPHERE -> doubleValue / 1013.25
      PressureUnit.MILLIMETERS_OF_MERCURY -> doubleValue * 0.750062
      PressureUnit.INCHES_OF_MERCURY -> doubleValue * 0.02953
    }
  }

  /**
   * Converts distance from meters to the specified unit.
   */
  fun convertDistance(value: Double, toUnit: DistanceUnit): Double {
    return when (toUnit) {
      DistanceUnit.METERS -> value
      DistanceUnit.KILOMETERS -> value / 1000.0
      DistanceUnit.MILES -> value / 1609.34
      DistanceUnit.NAUTICAL_MILES -> value / 1852.0
      DistanceUnit.FEET -> value * 3.28084
    }
  }

  /**
   * Converts precipitation from millimeters to the specified unit.
   */
  fun convertPrecipitation(value: Double, toUnit: PrecipitationUnit): Double {
    return when (toUnit) {
      PrecipitationUnit.MILLIMETERS, PrecipitationUnit.LITERS_PER_SQUARE_METER -> value
      PrecipitationUnit.CENTIMETERS -> value / 10.0
      PrecipitationUnit.INCHES -> value / 25.4
    }
  }
}
