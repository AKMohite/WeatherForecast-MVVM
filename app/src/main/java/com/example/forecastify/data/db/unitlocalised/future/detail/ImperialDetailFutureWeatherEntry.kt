package com.example.forecastify.data.db.unitlocalised.future.detail

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class ImperialDetailFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "maxtempF")
    override val maxtemp: Double,
    @ColumnInfo(name = "mintempF")
    override val mintemp: Double,
    @ColumnInfo(name = "avgtempF")
    override val avgtemp: Double,
    @ColumnInfo(name = "conditionText")
    override val conditionText: String,
    @ColumnInfo(name = "conditionIconUrl")
    override val conditionIconUrl: String,
    @ColumnInfo(name = "maxWindSpeedMps")
    override val maxWindSpeed: Double,
    @ColumnInfo(name = "precipIn")
    override val precip: Double,
    @ColumnInfo(name = "avgvisMilesMi")
    override val avgVisibilityDistance: Double,
    @ColumnInfo(name = "uvIndex")
    override val uvIndex: Double
) : UnitDetailFutureWeatherEntry