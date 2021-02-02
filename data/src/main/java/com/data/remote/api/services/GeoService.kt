package com.data.remote.api.services


import com.data.common.CITY_API_BASE_URL
import com.data.remote.entity.city.CityResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GeoService : Service {
    @GET("geo/cities")
    suspend fun loadCities(
        @Query("namePrefix") namePrefix: String,
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("distanceUnit") distanceUnit: String,
        @Query("languageCode") languageCode: String,
        @Query("limit") limit: String,
        @Query("sort") sort: String,
        @Query("types") types: String,
        @Query("hateoasMode") hateoasMode: String,
        @Header("x-rapidapi-host") api: String,
        @Header("x-rapidapi-key") apiKey: String
    ): Response<CityResponse>

    companion object Factory {
        fun create(): GeoService {
            val retrofit = Retrofit.Builder()
                .baseUrl(CITY_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(GeoService::class.java)
        }
    }
}

