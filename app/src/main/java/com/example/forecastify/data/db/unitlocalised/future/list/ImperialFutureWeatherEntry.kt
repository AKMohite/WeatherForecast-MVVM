package com.example.forecastify.data.db.unitlocalised.future.list

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

class ImperialFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avgtempF")
    override val avgtemp: Double,
    @ColumnInfo(name = "conditionText")
    override val conditionText: String,
    @ColumnInfo(name = "conditionIconUrl")
    override val conditionIconUrl: String
) : UnitSpecificFutureEntry