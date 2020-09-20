package com.example.forecastify.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "future_weather", indices = [Index(value = ["date"], unique = true)])
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    /*@Embedded(prefix = "astro_") // prefix the columns for astro tale
    val astro: Astro,*/
    val avgtempC: Double,
    val avgtempF: Double,
    val date: String,
//    @SerializedName("date_epoch")
//    val dateEpoch: Int,
//    val hourly: List<Hourly>,
    val maxtempC: Double,
    val maxtempF: Double,
    val mintempC: Double,
    val mintempF: Double,
    val sunhour: Double,
    val totalsnow: Int,
    val maxWindSpeedKmph: Double,
    val maxWindSpeedMps: Double,
    val precipMm: Double,
    val precipIn: Double,
    val avgvisMilesKm: Double,
    val avgvisMilesMi: Double,
    val conditionIconUrl: String,
    val conditionText: String,
    @SerializedName("uv_index")
    val uvIndex: Int
)