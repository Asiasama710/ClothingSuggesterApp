package com.asiasama.clothingsuggesterapp.modle.remote

import com.asiasama.clothingsuggesterapp.modle.responce.Clothing
import com.asiasama.clothingsuggesterapp.modle.responce.WeatherResponce
import io.reactivex.rxjava3.core.Single

interface ApiService {

    fun getClothes(temperature: Int): List<Clothing>
    fun getWeatherByCountryId(country: String?): Single<WeatherResponce>
    fun getWeatherLocation(lon: String, lat: String?): Single<WeatherResponce>
    fun getWeatherByCountryName(country: String?): Single<WeatherResponce>

}