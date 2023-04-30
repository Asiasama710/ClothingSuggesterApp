package com.asiasama.clothingsuggesterapp.data.remote.responce

import com.google.gson.annotations.SerializedName

data class WeatherStatus(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String
)
