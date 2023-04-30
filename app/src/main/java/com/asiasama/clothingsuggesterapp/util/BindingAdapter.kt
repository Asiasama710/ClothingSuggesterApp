package com.asiasama.clothingsuggesterapp.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.asiasama.clothingsuggesterapp.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso


@BindingAdapter(value = ["app:iconUrl"])
fun iconUrl(image: ImageView, icon: String?) {
    val url = image.context.getString(R.string.iconLink, icon)
    //  val url = "https://openweathermap.org/img/wn/$icon@2x.png"
    return Picasso.get().load(url).into(image)
}

@BindingAdapter(value = ["app:imageUrl"])
fun imageUrl(image: ShapeableImageView, url: String?) {
    return Picasso.get().load(url).into(image)
}