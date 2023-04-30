package com.asiasama.clothingsuggesterapp.data.repository

import com.asiasama.clothingsuggesterapp.data.local.ClothesDatasource
import com.asiasama.clothingsuggesterapp.data.remote.responce.Clothing
import com.asiasama.clothingsuggesterapp.data.remote.responce.WeatherResponce
import com.asiasama.clothingsuggesterapp.data.remote.weather_service.Network
import com.asiasama.clothingsuggesterapp.data.remote.weather_service.WeatherApiService
import io.reactivex.rxjava3.core.Single

class MainRepositoryImp : MainRepository {

    private val apiService: WeatherApiService = Network.apiService
    private val localClothes = ClothesDatasource()


    override fun getWeatherByCountryId(countryId: String): Single<WeatherResponce> {
        return apiService.getWeatherByCountryId(countryId)
    }

    override fun getWeatherLocation(latitude: String, longitude: String): Single<WeatherResponce> {
        return apiService.getWeatherLocation(longitude, latitude)
    }

    override fun getWeatherByCountryName(country: String): Single<WeatherResponce> {
        return apiService.getWeatherByCountryName(country)

    }

    override fun getLocalClothes(temperature: Int): List<Clothing> {
        return localClothes.getClothes(temperature)
    }


}