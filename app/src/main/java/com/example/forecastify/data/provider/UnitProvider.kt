package com.example.forecastify.data.provider

import com.example.forecastify.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}