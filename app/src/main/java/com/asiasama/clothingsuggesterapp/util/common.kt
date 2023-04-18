package com.asiasama.clothingsuggesterapp.util

import com.google.gson.Gson
import okhttp3.Response

fun String.convertToCelyzy(): String {
    val celezy = (this.toFloat() - 273.15)
    return "%.0f".format(celezy)
}


fun <T> Response.toJson(response: Class<T>): T {
    return this.body?.string().let { jsonString ->
        Gson().fromJson(jsonString, response)
    }
}




