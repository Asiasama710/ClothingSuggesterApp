package com.asiasama.clothingsuggesterapp.data.repository

import com.asiasama.clothingsuggesterapp.data.responce.Clothing
import com.asiasama.clothingsuggesterapp.data.responce.WeatherResponce
import io.reactivex.rxjava3.core.Single

interface MainRepository {

    fun getWeatherLocation(latitude: String, longitude: String): Single<WeatherResponce>
    fun getWeatherByCountryName(country: String): Single<WeatherResponce>
    fun getClothes(temperature: Int): List<Clothing>

}