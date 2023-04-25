package com.asiasama.clothingsuggesterapp.modle

import com.asiasama.clothingsuggesterapp.modle.responce.Clothing
import com.asiasama.clothingsuggesterapp.modle.responce.WeatherResponce
import okio.IOException

interface ApiService {


    fun weatherData(
        onSuccess: (WeatherResponce) -> Unit,
        onFailure: (IOException) -> Unit,
        country: String
    )

    fun getClothes(temperature: Int): List<Clothing>

}