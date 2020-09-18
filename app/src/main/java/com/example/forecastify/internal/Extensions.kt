package com.example.forecastify.internal

import kotlin.math.roundToInt

fun Double?.convertMetricToImperial(unitType: UnitType): Double{
    val metricValue = this ?: 0.0
    var imperialValue: Double = when(unitType){
        UnitType.TEMPERATURE -> {
            (metricValue * 9/5) + 32 // C -> F
        }

        UnitType.WIND_SPEED -> {
            metricValue / 3.6 // kmph -> mps
        }

        UnitType.PRECIPITATION -> {
            metricValue / 25.4 // mm -> in
        }

        UnitType.VISIBILITY -> {
            metricValue / 1.609 // km -> mile
        }
    }

    imperialValue = ((imperialValue * 100).roundToInt() / 100).toDouble()

    return imperialValue
}


fun Double?.convertImperialToMetric(unitType: UnitType): Double{
    val imperialValue = this ?: 0.0
    var metricValue: Double = when(unitType){
        UnitType.TEMPERATURE -> {
            (imperialValue - 32) * 5/9 // F -> C
        }

        UnitType.WIND_SPEED -> {
            imperialValue * 3.6 // mps -> kmph
        }

        UnitType.PRECIPITATION -> {
            imperialValue * 25.4 // in -> mm
        }

        UnitType.VISIBILITY -> {
            imperialValue * 1.609 // mile -> km
        }
    }

    metricValue = ((metricValue * 100).roundToInt() / 100).toDouble()

    return metricValue
}