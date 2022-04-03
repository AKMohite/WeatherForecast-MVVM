package com.example.forecastify.data.network

import okhttp3.Interceptor
import okhttp3.Response

class MockInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("Not yet implemented")
    }
}