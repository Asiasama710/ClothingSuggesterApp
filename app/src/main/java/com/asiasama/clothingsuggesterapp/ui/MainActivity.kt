package com.asiasama.clothingsuggesterapp.ui

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.asiasama.clothingsuggesterapp.R
import com.asiasama.clothingsuggesterapp.data.model.Clothing
import com.asiasama.clothingsuggesterapp.util.PrefsUtil.initSharedPreferences
import com.asiasama.clothingsuggesterapp.databinding.ActivityMainBinding
import com.asiasama.clothingsuggesterapp.data.remote.WeatherApiService
import com.asiasama.clothingsuggesterapp.data.remote.ClothesDatasource
import com.asiasama.clothingsuggesterapp.data.model.WeatherResponce
import com.asiasama.clothingsuggesterapp.util.PrefsUtil
import com.asiasama.clothingsuggesterapp.util.PrefsUtil.loadImage
import com.asiasama.clothingsuggesterapp.util.PrefsUtil.saveImage
import com.asiasama.clothingsuggesterapp.util.convertToCelyzy
import com.asiasama.clothingsuggesterapp.util.toJson
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException

class MainActivity : AppCompatActivity(), Callback {

    private lateinit var binding: ActivityMainBinding
    private var getClothes: ClothesDatasource = ClothesDatasource()
    private val apiService = WeatherApiService(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSharedPreferences(this)
        apiService.makeRequestWeather("iraq")
        findCity()


    }

    private fun findCity() {
        binding.inputName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.inputName.text.toString().isNotEmpty()) {
                    apiService.makeRequestWeather(binding.inputName.text.toString())
                } else {
                    Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        Log.i("TAG", "fail:${e.message}")

    }

    override fun onResponse(call: Call, response: Response) {
        val result = response.toJson(WeatherResponce::class.java)
        runOnUiThread { updateUi(result) }
    }


    private fun updateUi(result: WeatherResponce) {
        binding.apply {
            temp.text = root.context.getString(
                R.string.temperature,
                result.main?.temperature?.convertToCelyzy()
            )
            val randomImage =
                result.main?.temperature?.convertToCelyzy()?.toInt()
                    ?.let { getClothes.getClothes(it).random() }
            Picasso.get()
                .load(root.context.getString(R.string.iconLink, result.weather.first().icon))
                .into(iconWeather)

            if (PrefsUtil.image.isNullOrEmpty()) { // if there is no last worn clothes
                getClothesLocal(randomImage!!.image, imageClothis)
            } else if (PrefsUtil.image != randomImage!!.image) { // if the last worn clothes is not the same as the new one
                getClothesLocal(randomImage.image, imageClothis)
            } else if (PrefsUtil.image == randomImage.image) { // if the last worn clothes is the same as the new one
                getClothesLocal(randomImage.image, imageClothis)
            }

        }
    }

    private fun getClothesLocal(randomImage: String, drawable: ShapeableImageView) {
        saveImage(randomImage)
        Picasso.get().load(loadImage()).into(drawable)
    }


}












