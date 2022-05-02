package com.example.forecastify.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.example.forecastify.internal.LocationPermissionNotGrantedException
import com.example.forecastify.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import kotlin.math.abs

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"
class LocationProviderImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext context: Context
) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocationEntry): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    override suspend fun getPreferredLocationString(): Pair<Double, Double> {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return getCustomLocationName()
                return Pair(deviceLocation.latitude, deviceLocation.longitude)
            } catch (e: LocationPermissionNotGrantedException) {
                return getCustomLocationName()
            }
        }
        else
            return getCustomLocationName()
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocationEntry): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        // Comparing doubles cannot be done with "=="
        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - lastWeatherLocation.lat.toDouble()) > comparisonThreshold &&
                abs(deviceLocation.longitude - lastWeatherLocation.lon.toDouble()) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocationEntry): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName.first != lastWeatherLocation.lat.toDouble() && customLocationName.second != lastWeatherLocation.lon.toDouble()
        }
        return false
    }

    private fun getCustomLocationName(): Pair<Double, Double> {
//        return preferences.getString(CUSTOM_LOCATION, null)
        return Pair(19.0181129, 73.086464)
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}