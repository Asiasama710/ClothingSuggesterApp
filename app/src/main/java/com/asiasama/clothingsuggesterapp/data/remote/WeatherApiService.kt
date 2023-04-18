package com.asiasama.clothingsuggesterapp.data.remote

import com.asiasama.clothingsuggesterapp.BuildConfig
import okhttp3.*

class WeatherApiService(
    private val call: Callback,
) {

    private val client = OkHttpClient()

    fun makeRequestWeather(country: String?) {
        val response = HttpUrl.Builder()
            .scheme(SCHEME)
            .host(BuildConfig.HOST)
            .addPathSegment(PATH)
            .addQueryParameter(QUERY, country)
            .addQueryParameter(APP_ID_Q, BuildConfig.APP_ID)
            .build()

        val request = Request.Builder().url(response).build()

        client.newCall(request).enqueue(call)
    }

    companion object {
        const val SCHEME = "https"
        const val PATH = "/data/2.5/weather"
        const val QUERY = "q"
        const val APP_ID_Q = "appid"
    }
}