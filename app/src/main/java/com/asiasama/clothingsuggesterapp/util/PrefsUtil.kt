package com.asiasama.clothingsuggesterapp.util

import android.content.Context
import android.content.SharedPreferences

object PrefsUtil {
    private var sharedPreferences: SharedPreferences? = null


    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var image: String?
        get() = sharedPreferences?.getString(LAST_WORN_CLOTHES, "")
        set(value) {
            value?.let { sharedPreferences?.edit()?.putString(LAST_WORN_CLOTHES, it)?.apply() }
        }


    fun loadImage(): String? {
        return this.image
    }

    fun saveImage(image: String) {
        this.image = image
    }

    private const val PREFS_NAME = "MyPrefsFile"
    private const val LAST_WORN_CLOTHES = "lastWornClothes"

}