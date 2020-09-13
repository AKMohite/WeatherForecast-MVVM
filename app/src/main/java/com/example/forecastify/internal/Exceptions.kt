package com.example.forecastify.internal

import java.io.IOException

class NoInternetConnectivityException: IOException()
class LocationPermissionNotGrantedException: IOException()
class DateNotFoundException: IOException()