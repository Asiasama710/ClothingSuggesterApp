package com.asiasama.clothingsuggesterapp.ui.presenter

import com.asiasama.clothingsuggesterapp.modle.ApiService
import com.asiasama.clothingsuggesterapp.modle.ApiServiceImpl
import com.asiasama.clothingsuggesterapp.modle.responce.WeatherResponce
import com.asiasama.clothingsuggesterapp.util.UiState
import okio.IOException

class HomePresenter(
    private val view: IViewHome,
) {
    private val apiService: ApiService = ApiServiceImpl()

    fun loadWeatherData(country: String) {
        view.weatherStatus(UiState.Loading)
        apiService.weatherData(::onWeatherDataSuccess, ::onWeatherDataFailure, country)
    }

    private fun onWeatherDataSuccess(weather: WeatherResponce) {
        view.weatherStatus(UiState.Success(weather))
    }

    private fun onWeatherDataFailure(e: IOException) {
        view.weatherStatus(UiState.Error(e.message.toString()))
    }
}