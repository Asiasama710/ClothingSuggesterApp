package com.asiasama.clothingsuggesterapp.data.repository

import com.asiasama.clothingsuggesterapp.data.remote.responce.Clothing
import com.asiasama.clothingsuggesterapp.data.remote.responce.WeatherResponce
import io.reactivex.rxjava3.core.Single

interface MainRepository {

    fun getWeatherLocation(latitude: String, longitude: String): Single<WeatherResponce>
    fun getWeatherByCountryName(country: String): Single<WeatherResponce>
    fun getWeatherByCountryId(countryId: String): Single<WeatherResponce>
    fun getLocalClothes(temperature: Int): List<Clothing>

}