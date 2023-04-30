package com.asiasama.clothingsuggesterapp.data.remote.weather_service

import com.asiasama.clothingsuggesterapp.BuildConfig
import com.asiasama.clothingsuggesterapp.data.remote.responce.WeatherResponce
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    companion object {
        private const val UNITS = "metric"
    }

    @GET("weather")
    fun getWeatherByCountryName(
        @Query("q") country: String?,
        @Query("units") units: String = UNITS,
        @Query("appid") appid: String = BuildConfig.APP_ID,
    ): Single<WeatherResponce>

    @GET("weather")
    fun getWeatherLocation(
        @Query("lon") lon: String,
        @Query("lat") lat: String,
        @Query("units") units: String = UNITS,
        @Query("appid") appid: String = BuildConfig.APP_ID,
    ): Single<WeatherResponce>

    @GET("weather")
    fun getWeatherByCountryId(
        @Query("id") countryId: String?,
        @Query("appid") appid: String = BuildConfig.APP_ID,
    ): Single<WeatherResponce>
}