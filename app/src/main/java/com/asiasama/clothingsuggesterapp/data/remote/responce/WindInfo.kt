package com.asiasama.clothingsuggesterapp.data.remote.responce

import com.google.gson.annotations.SerializedName


data class WindInfo(
    @SerializedName("speed")
    val wind_speed: String,
    @SerializedName("deg")
    val wind_direction: String,
    @SerializedName("gust")
    val wind_gust: String

)
