package com.asiasama.clothingsuggesterapp.data.model

import com.google.gson.annotations.SerializedName


data class WeatherResponce(
    @SerializedName("name")
    val name: String,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("wind")
    val wind: WindInfo,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("cod")
    val cod: Int
)