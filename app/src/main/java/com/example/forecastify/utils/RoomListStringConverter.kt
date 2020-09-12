package com.example.forecastify.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

//https://stackoverflow.com/a/62692256
//https://youtu.be/SB0UepZ5lT4?list=PLB6lc7nQ1n4jTLDyU2muTBo8xk0dg0D_w&t=601

class RoomListStringConverter {
    @TypeConverter
    fun fromArray(strings: List<String>): String {
        return strings.joinToString(",")
    }

    @TypeConverter
    fun toArray(concatenatedStrings: String) : List<String>{
        return concatenatedStrings.split(",")
    }
}