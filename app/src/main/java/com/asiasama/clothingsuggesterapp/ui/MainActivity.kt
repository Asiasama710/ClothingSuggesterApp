package com.asiasama.clothingsuggesterapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.asiasama.clothingsuggesterapp.R
import com.asiasama.clothingsuggesterapp.databinding.ActivityMainBinding
import com.asiasama.clothingsuggesterapp.modle.remote.ClothesDatasource
import com.asiasama.clothingsuggesterapp.modle.responce.WeatherResponce
import com.asiasama.clothingsuggesterapp.ui.presenter.HomePresenter
import com.asiasama.clothingsuggesterapp.ui.presenter.IViewHome
import com.asiasama.clothingsuggesterapp.util.PrefsUtil
import com.asiasama.clothingsuggesterapp.util.PrefsUtil.initSharedPreferences
import com.asiasama.clothingsuggesterapp.util.PrefsUtil.loadImage
import com.asiasama.clothingsuggesterapp.util.PrefsUtil.saveImage
import com.asiasama.clothingsuggesterapp.util.UiState
import com.asiasama.clothingsuggesterapp.util.convertToCelyzy
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), IViewHome {

    private lateinit var binding: ActivityMainBinding
    private var getClothes: ClothesDatasource = ClothesDatasource()
    private val homePresenter = HomePresenter(this)
//    private val apiService = WeatherApiService(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSharedPreferences(this)
        homePresenter.loadWeatherData("iraq")
        findCity()
    }

    override fun weatherStatus(status: UiState) {
        runOnUiThread {
            when (status) {
                is UiState.Loading -> {
                    binding.progressBarLoading.isVisible = true
                }

                is UiState.Success -> {
                    binding.progressBarLoading.isGone = true
                    updateUi(status.data as WeatherResponce)
                }

                is UiState.Error -> {
                    binding.progressBarLoading.isVisible = false
                    Toast.makeText(this, status.error, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    @SuppressLint("CheckResult")
    private fun findCity() {
        Observable.create { emitter ->
            binding.inputName.doOnTextChanged { text, _, _, _ ->
                emitter.onNext(text.toString())
            }
        }.debounce(1, TimeUnit.SECONDS)
         .subscribe { homePresenter.loadWeatherData(it)
             Log.i("TAG", "onNext: $it")
         }
    }


    private fun updateUi(result: WeatherResponce) {
        runOnUiThread {
            binding.apply {
                val temperature = result.main?.temperature?.convertToCelyzy()
                textViewTemp.text = root.context.getString(R.string.temperature, temperature ?: 0)
                textViewTempDescription.text = result.weather?.first()?.description
                var randomImage =
                    temperature?.toInt().let { getClothes.getClothes(it ?: 0).random() }
                Picasso.get()
                    .load(root.context.getString(R.string.iconLink, result.weather?.first()?.icon))
                    .into(iconWeather)

                if (PrefsUtil.image.isNullOrEmpty()) { // if there is no last worn clothes
                    updateSuggestionsClothes(randomImage.image, imageClothis)
                } else if (PrefsUtil.image != randomImage.image) { // if the last worn clothes is not the same as the new one
                    updateSuggestionsClothes(randomImage.image, imageClothis)
                } else {
                    randomImage =
                        temperature?.toInt().let { getClothes.getClothes(it ?: 0).random() }
                    updateSuggestionsClothes(randomImage.image, imageClothis)
                }
            }
        }
    }

    private fun updateSuggestionsClothes(randomImage: String, drawable: ShapeableImageView) {
        saveImage(randomImage)
        Picasso.get().load(loadImage()).into(drawable)
    }


}












