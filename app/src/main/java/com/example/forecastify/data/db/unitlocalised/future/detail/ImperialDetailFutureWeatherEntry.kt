package com.example.forecastify.data.db.unitlocalised.future.detail

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class ImperialDetailFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "maxtemp")
    override val maxtemp: Double,
    @ColumnInfo(name = "mintemp")
    override val mintemp: Double,
    @ColumnInfo(name = "avgtemp")
    override val avgtemp: Double,
    @ColumnInfo(name = "conditionText")
    override val conditionText: String,
    @ColumnInfo(name = "conditionIconUrl")
    override val conditionIconUrl: String,
    @ColumnInfo(name = "maxWindSpeed")
    override val maxWindSpeed: Double,
    @ColumnInfo(name = "precip")
    override val precip: Double,
    @ColumnInfo(name = "avgvisMiles")
    override val avgVisibilityDistance: Double,
    @ColumnInfo(name = "uvIndex")
    override val uvIndex: Double
) : UnitDetailFutureWeatherEntry