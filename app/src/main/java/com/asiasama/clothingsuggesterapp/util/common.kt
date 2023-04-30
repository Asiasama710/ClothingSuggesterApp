package com.asiasama.clothingsuggesterapp.util

import android.widget.ImageView
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Response
import androidx.databinding.BindingAdapter
import com.asiasama.clothingsuggesterapp.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.core.Observable

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

fun <T : Any> Observable<T>.handleThreadsAndSubscribe(
    onSuccess: (it: T) -> Unit, onError: (e: Throwable) -> Unit,
): Disposable {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onError)

}

fun <T : Any> Single<T>.handleThreadsAndSubscribe(
    onSuccess: (it: T) -> Unit, onError: (e: Throwable) -> Unit,
): Disposable {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onError)
}



