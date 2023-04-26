package com.asiasama.clothingsuggesterapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), IViewHome {

    private lateinit var binding: ActivityMainBinding
    private var getClothes: ClothesDatasource = ClothesDatasource()
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val homePresenter = HomePresenter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSharedPreferences(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
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
         .subscribe { homePresenter.loadWeatherDataByCountryName(it)
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

    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { location ->
                    val location: Location? = location.result
                    if (location != null) {
                        Toast.makeText(this, "Get Success", Toast.LENGTH_SHORT).show()
                        homePresenter.loadWeatherData(location.latitude.toString(), location.longitude.toString())
                    } else {
                        Toast.makeText(this, "Null Received", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location ", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
           PERMISSION_PEQEST_ACCESS_LOCATION
        )
    }
    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
            getCurrentLocation()
        } else {
            Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PERMISSION_PEQEST_ACCESS_LOCATION = 100
    }


}












