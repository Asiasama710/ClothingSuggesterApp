package com.asiasama.clothingsuggesterapp.data.model

import com.google.gson.annotations.SerializedName

data class Clothing(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("temperature")
    val temperature: String,
    @SerializedName("image")
    val image: String
)
