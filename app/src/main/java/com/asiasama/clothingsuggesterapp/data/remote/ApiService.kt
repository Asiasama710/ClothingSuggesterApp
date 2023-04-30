package com.asiasama.clothingsuggesterapp.data.remote

import com.asiasama.clothingsuggesterapp.data.responce.WeatherResponce
import io.reactivex.rxjava3.core.Single

interface ApiService {

    fun getWeatherByCountryId(country: String?): Single<WeatherResponce>
    fun getWeatherLocation(lon: String, lat: String?): Single<WeatherResponce>
    fun getWeatherByCountryName(country: String?): Single<WeatherResponce>

}