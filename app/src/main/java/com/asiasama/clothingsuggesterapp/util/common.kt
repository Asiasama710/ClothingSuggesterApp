package com.asiasama.clothingsuggesterapp.util

import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
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

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}



