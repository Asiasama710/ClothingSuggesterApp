package com.asiasama.clothingsuggesterapp.data.model

import com.google.gson.annotations.SerializedName


data class WeatherResponce(
    @SerializedName("name")
    val name: String,
    @SerializedName("main")
    val main: WeatherInformation,
    @SerializedName("wind")
    val wind: WindInfo,
    @SerializedName("weather")
    val weather: List<WeatherStatus>,
    @SerializedName("cod")
    val cod: Int
)