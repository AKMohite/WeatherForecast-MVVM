package com.example.forecastify.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.forecastify.data.network.response.Astro
import com.google.gson.annotations.SerializedName

@Entity(tableName = "future_weather", indices = [Index(value = ["date"], unique = true)])
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @Embedded(prefix = "astro_") // prefix the columns for astro tale
    val astro: Astro,
    val avgtemp: Double,
    val date: String,
//    @SerializedName("date_epoch")
//    val dateEpoch: Int,
//    val hourly: List<Hourly>,
    val maxtemp: Int,
    val mintemp: Int,
    val sunhour: Double,
    val totalsnow: Int,
    val conditionIconUrl: String,
    val conditionText: String,
    @SerializedName("uv_index")
    val uvIndex: Int
)