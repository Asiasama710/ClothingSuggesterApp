package com.asiasama.clothingsuggesterapp.data.repository

import com.asiasama.clothingsuggesterapp.data.remote.ApiServiceImpl
import com.asiasama.clothingsuggesterapp.data.remote.ClothesDatasource
import com.asiasama.clothingsuggesterapp.data.responce.Clothing
import com.asiasama.clothingsuggesterapp.data.responce.WeatherResponce
import io.reactivex.rxjava3.core.Single

class MainRepositoryImp() : MainRepository {

    private val localClothes = ClothesDatasource()
    private val apiService = ApiServiceImpl()


    override fun getWeatherLocation(latitude: String, longitude: String): Single<WeatherResponce> {
        return apiService.getWeatherLocation(longitude, latitude)

    }

    override fun getWeatherByCountryName(country: String): Single<WeatherResponce> {
        return apiService.getWeatherByCountryName(country)

    }

    override fun getClothes(temperature: Int): List<Clothing> {
        return localClothes.getClothes(temperature)
    }


}