package com.asiasama.clothingsuggesterapp.ui.presenter

import android.annotation.SuppressLint
import com.asiasama.clothingsuggesterapp.modle.remote.ApiServiceImpl
import com.asiasama.clothingsuggesterapp.modle.responce.WeatherResponce
import com.asiasama.clothingsuggesterapp.util.UiState
import com.asiasama.clothingsuggesterapp.util.addTo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomePresenter(
    private val view: IViewHome,
) {
    private val apiService = ApiServiceImpl()
    private var compositeDisposable = CompositeDisposable()


    @SuppressLint("CheckResult")
    fun loadWeatherData(latitude: String, longitude: String) {
        apiService.getWeatherLocation(longitude,latitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onWeatherDataSuccess, ::onWeatherDataFailure)
            .addTo(compositeDisposable)
    }
    fun loadWeatherDataByCountryName(country: String) {
        apiService.getWeatherByCountryName(country)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onWeatherDataSuccess, ::onWeatherDataFailure)
            .addTo(compositeDisposable)
    }



    private fun onWeatherDataSuccess(weather: WeatherResponce) {
        view.weatherStatus(UiState.Success(weather))
    }

    private fun onWeatherDataFailure(e: Throwable) {
        view.weatherStatus(UiState.Error(e.message.toString()))
    }

}