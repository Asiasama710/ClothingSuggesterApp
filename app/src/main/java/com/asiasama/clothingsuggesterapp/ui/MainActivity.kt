package com.asiasama.clothingsuggesterapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.asiasama.clothingsuggesterapp.R
import com.asiasama.clothingsuggesterapp.data.remote.responce.WeatherResponce
import com.asiasama.clothingsuggesterapp.databinding.ActivityMainBinding
import com.asiasama.clothingsuggesterapp.util.PrefsUtil.initSharedPreferences
import com.asiasama.clothingsuggesterapp.util.convertToCelyzy
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        binding.viewModel = viewModel
        initSharedPreferences(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()
//        viewModel.weatherStatus.observe(this) {
//            updateUi(it)
//        }
        viewModel.clothes.observe(this) {
            Picasso.get().load(it).into(binding.imageClothis)
        }
        viewModel.cityName.observe(this) {
            viewModel.getWeatherDataByCountryName(it)
        }

    }

    private fun updateUi(result: WeatherResponce) {
        val temperature = result.main?.temperature?.convertToCelyzy()?.toInt()

        binding.apply {
            textViewTemp.text = root.context.getString(R.string.temperature, temperature.toString())
            textViewTempDescription.text = result.weather?.first()?.description
            Picasso.get()
                .load(root.context.getString(R.string.iconLink, result.weather?.first()?.icon))
                .into(iconWeather)
        }
        viewModel.getLocalClothes(temperature ?: 0)

    }


    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { location ->
                    val location: Location? = location.result
                    if (location != null) {
                        Toast.makeText(this, "Get Success", Toast.LENGTH_SHORT).show()
                        viewModel.getWeatherData(
                            latitude = location.longitude.toString(),
                            longitude = location.latitude.toString()
                        )
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
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
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












