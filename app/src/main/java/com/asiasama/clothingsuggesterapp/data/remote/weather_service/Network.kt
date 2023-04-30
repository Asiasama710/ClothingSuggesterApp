package com.asiasama.clothingsuggesterapp.data.remote.weather_service

import com.asiasama.clothingsuggesterapp.data.remote.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private val client = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build()
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
    val apiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)
}