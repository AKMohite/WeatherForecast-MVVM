package com.example.forecastify.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.forecastify.utils.RoomListStringConverter
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    val cloudcover: Int,
    @SerializedName("feelslike")
    val feelslikeC: Double,
    var feelslikeF: Double,
    val humidity: Int,
    @SerializedName("is_day")
    val isDay: String,
    @SerializedName("observation_time")
    val observationTime: String,
    @SerializedName("precip")
    val precipMm: Double,
    var precipIn: Double,
    val pressure: Int,
    @SerializedName("temperature")
    val temperatureC: Double,
    var temperatureF: Double,
    @SerializedName("uv_index")
    val uvIndex: Int,
    @SerializedName("visibility")
    val visibilityKm: Double,
    var visibilityMi: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("weather_descriptions")
    @TypeConverters(RoomListStringConverter::class)
    val weatherDescriptions: List<String>?,
    @SerializedName("weather_icons")
    @TypeConverters(RoomListStringConverter::class)
    val weatherIcons: List<String>?,
    @SerializedName("wind_degree")
    val windDegree: Int,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeedKmph: Double,
    var windSpeedMps: Double
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}