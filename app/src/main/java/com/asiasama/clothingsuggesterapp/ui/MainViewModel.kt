package com.asiasama.clothingsuggesterapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asiasama.clothingsuggesterapp.data.repository.MainRepositoryImp
import com.asiasama.clothingsuggesterapp.data.responce.WeatherResponce
import com.asiasama.clothingsuggesterapp.util.PrefsUtil
import com.asiasama.clothingsuggesterapp.util.addTo
import com.asiasama.clothingsuggesterapp.util.handleThreadsAndSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    private val repository = MainRepositoryImp()
    private var compositeDisposable = CompositeDisposable()

    private val _weatherStatus = MutableLiveData<WeatherResponce>()
    val weatherStatus: LiveData<WeatherResponce> get() = _weatherStatus

    private val _clothes = MutableLiveData<String>()
    val clothes: LiveData<String> get() = _clothes

    val cityName = MutableLiveData<String>()

    fun getWeatherData(longitude: String, latitude: String) {
        repository.getWeatherLocation(longitude, latitude)
            .handleThreadsAndSubscribe(::onWeatherDataSuccess, ::onWeatherDataFailure)
            .addTo(compositeDisposable)
    }

    fun getWeatherDataByCountryName(country: String) {
        return repository.getWeatherByCountryName(country).toObservable()
            .debounce(500, TimeUnit.MILLISECONDS)
            .handleThreadsAndSubscribe(::onWeatherDataSuccess, ::onWeatherDataFailure)
            .addTo(compositeDisposable)
    }

    private fun onWeatherDataSuccess(weather: WeatherResponce?) {
        _weatherStatus.postValue(weather)
    }

    private fun onWeatherDataFailure(e: Throwable) {
        Log.e("TAG", "onWeatherDataFailure: ${e.message}")
    }

    fun getLocalClothes(temperature: Int) {
        var suggestCloth = repository.getClothes(temperature).random().image
        if (PrefsUtil.image.isNullOrEmpty()) { // if there is no last worn clothes
            PrefsUtil.saveImage(suggestCloth)
        } else if (PrefsUtil.image != suggestCloth) { // if the last worn clothes is not the same as the new one
            updateSuggestionsClothes(suggestCloth)
        }
        _clothes.postValue(PrefsUtil.image)
    }

    private fun updateSuggestionsClothes(newCloth: String) {
        while (newCloth != PrefsUtil.image) {
            PrefsUtil.saveImage(newCloth)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}


