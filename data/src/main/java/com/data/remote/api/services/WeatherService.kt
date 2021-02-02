package com.data.remote.api.services

import com.data.common.WEATHER_API_BASE_URL
import com.data.remote.entity.WeatherResponsePOJO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService : Service {
    @GET("onecall")
   suspend fun loadWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Response<WeatherResponsePOJO>

    companion object Factory {
        fun create(): WeatherService {
            val retrofit = Retrofit.Builder()
                .baseUrl(WEATHER_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(WeatherService::class.java)
        }
    }
}
