package com.asiasama.clothingsuggesterapp.modle

import android.util.Log
import com.asiasama.clothingsuggesterapp.modle.responce.Clothing
import com.asiasama.clothingsuggesterapp.modle.responce.WeatherResponce
import com.asiasama.clothingsuggesterapp.modle.remote.ClothesDatasource
import com.asiasama.clothingsuggesterapp.util.toJson
import okhttp3.*
import okio.IOException

class ApiServiceImpl : ApiService {
    private val client = OkHttpClient()


    //    private val apiService = ApiClient.getClient().create(ApiService::class.java)
//    private val weatherResponce = apiService.getWeatherData("Moscow")
    private val clothes = ClothesDatasource()

    override fun weatherData(
        onSuccess: (WeatherResponce) -> Unit,
        onFailure: (IOException) -> Unit,
        country: String
    ) {
        val responce = HttpUrl.Builder()
            .scheme("https")
            .host("api.openweathermap.org")
            .addPathSegment("/data/2.5/weather")
            .addQueryParameter("q", country)
            .addQueryParameter("appid", "8d27736fcb0eafc4d723f44873b44724")
            .build()

        val request = Request.Builder().url(responce).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {

                try {
                    val result = response.toJson(WeatherResponce::class.java)
                    onSuccess(result)
                }catch (e: IOException){
                    Log.e( "TAG","error in = ${e.message}")
                }

            }
        })


    }


    override fun getClothes(temperature: Int): List<Clothing> {
        return clothes.getClothes(temperature)
    }





}