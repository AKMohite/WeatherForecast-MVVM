package com.example.forecastify.data.db.unitlocalised.future.detail

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class MetricDetailFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "maxtempC")
    override val maxtemp: Double,
    @ColumnInfo(name = "mintempC")
    override val mintemp: Double,
    @ColumnInfo(name = "avgtempC")
    override val avgtemp: Double,
    @ColumnInfo(name = "conditionText")
    override val conditionText: String,
    @ColumnInfo(name = "conditionIconUrl")
    override val conditionIconUrl: String,
    @ColumnInfo(name = "maxWindSpeedKmph")
    override val maxWindSpeed: Double,
    @ColumnInfo(name = "precipMm")
    override val precip: Double,
    @ColumnInfo(name = "avgvisMilesKm")
    override val avgVisibilityDistance: Double,
    @ColumnInfo(name = "uvIndex")
    override val uvIndex: Double
) : UnitDetailFutureWeatherEntry