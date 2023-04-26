package com.asiasama.clothingsuggesterapp.modle.remote

import android.util.Log
import com.asiasama.clothingsuggesterapp.BuildConfig
import com.asiasama.clothingsuggesterapp.modle.responce.Clothing
import com.asiasama.clothingsuggesterapp.modle.responce.WeatherResponce
import com.asiasama.clothingsuggesterapp.util.toJson
import io.reactivex.rxjava3.core.Single
import okhttp3.*
import org.json.JSONObject

class ApiServiceImpl : ApiService {
    private val client = OkHttpClient()
    private val clothes = ClothesDatasource()
    override fun getWeatherLocation(lon: String, lat: String?): Single<WeatherResponce> {
        val url = HttpUrl.Builder()
            .scheme(SCHEME)
            .host(HOST)
            .addPathSegment(PATH)
            .addQueryParameter("lat", lat)
            .addQueryParameter("lon", lon)
            .addQueryParameter(APP_ID_Q, BuildConfig.APP_ID)
            .build()
        val request = Request.Builder().url(url).build()
        return Single.create { emitter ->
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val json = JSONObject(response.body?.string() ?: "")
                    val locationId = json.getString("id")
                    Log.i("TAG", "responseLocation = ${locationId}")
                    emitter.onSuccess(locationId)
                } else {
                    Log.i("TAG", "errorLocation")
                    emitter.onError(Throwable("Failed to get location ID for $lon, $lat"))
                }
            }
        }.flatMap {
            getWeatherByCountryId(it)
        }
    }

    override fun getWeatherByCountryId(countryId: String?): Single<WeatherResponce> {
        val url = HttpUrl.Builder()
            .scheme(SCHEME)
            .host(HOST)
            .addPathSegment(PATH)
            .addQueryParameter(QUNTRY_ID, countryId)
            .addQueryParameter(APP_ID_Q, BuildConfig.APP_ID)
            .build()
        val request = Request.Builder().url(url).build()

        return Single.create { emitter ->
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val result = response.toJson(WeatherResponce::class.java)
                Log.i("TAG", "response BY COUNTRY ID = ${result}")
                emitter.onSuccess(result)
            } else {
                Log.i("TAG", "response else counrty id = ${response.body?.string()}")
                emitter.onError(Throwable("Failed to get weather for location ID $countryId"))
            }
        }

    }

    override fun getWeatherByCountryName(country: String?): Single<WeatherResponce> {
        val url = HttpUrl.Builder()
            .scheme(SCHEME)
            .host(HOST)
            .addPathSegment(PATH)
            .addQueryParameter(QUERY, country)
            .addQueryParameter(APP_ID_Q, BuildConfig.APP_ID)
            .build()
        val request = Request.Builder().url(url).build()

        return Single.create { emitter ->
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val result = response.toJson(WeatherResponce::class.java)
                    Log.i("TAG", "response = ${result}")
                    emitter.onSuccess(result)
                } else {
                    Log.i("TAG", "response else = ${response.body?.string()}")

                    emitter.onError(Throwable("Failed to get weather for location ID $country"))
                }
            }
        }
    }
    
    override fun getClothes(temperature: Int): List<Clothing> {
        return clothes.getClothes(temperature)
    }

    companion object {
        const val SCHEME = "https"
        const val PATH = "/data/2.5/weather"
        const val QUERY = "q"
        const val QUNTRY_ID = "id"
        const val APP_ID_Q = "appid"
        const val HOST = "api.openweathermap.org"

    }


}