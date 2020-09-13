package com.example.forecastify.data.db.unitlocalised.future.list

import androidx.room.ColumnInfo
import com.example.forecastify.data.db.unitlocalised.future.list.UnitSpecificFutureEntry
import org.threeten.bp.LocalDate

class ImperialFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avgtemp")
    override val avgtemp: Double,
    @ColumnInfo(name = "conditionText")
    override val conditionText: String,
    @ColumnInfo(name = "conditionIconUrl")
    override val conditionIconUrl: String
) : UnitSpecificFutureEntry