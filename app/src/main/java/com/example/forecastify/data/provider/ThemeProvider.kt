package com.example.forecastify.data.provider


interface ThemeProvider{

    fun getThemeFromPreferences(): Int

    fun getThemeDescriptionForPreference(preferenceValue: String?): String

    fun getTheme(selectedTheme: String): Int
}