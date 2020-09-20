package com.example.forecastify.utils

import androidx.room.TypeConverter

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