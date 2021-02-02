package com.data.remote.api

import android.util.Log
import com.data.remote.api.services.GeoService
import com.data.remote.api.services.Service
import com.data.remote.api.services.WeatherService
import com.data.remote.entity.WeatherResponsePOJO
import com.data.remote.entity.city.CityResponse
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.coroutineContext

abstract class RetrofitRequest<T>(protected val params: Params) {
    protected val service: Service by lazy { createService() }

    suspend fun execute(): T {
        val response = loadData()
        return validate(response)
    }

    protected abstract fun createService(): Service
    protected abstract suspend fun loadData(): Response<T>
    protected abstract fun validate(response: Response<T>): T
}

class LoadCitiesRetrofitRequest(params: Params) : RetrofitRequest<CityResponse>(params) {
    override fun createService() = GeoService.create()

    override suspend fun loadData(): Response<CityResponse> {
        Log.d(
            "Tag", "$coroutineContext"
        )
        params as LoadCitiesParams
        return (service as GeoService).loadCities(
            namePrefix = params.namePrefix, location = params.location,
            radius = params.radius,
            distanceUnit = params.distanceUnit,
            languageCode = params.languageCode,
            limit = params.limit,
            sort = params.sort,
            types = params.types,
            hateoasMode = params.hateoasMode,
            api = params.api,
            apiKey = params.apiKey
        )
    }

    override fun validate(response: Response<CityResponse>): CityResponse {
        if (response.isSuccessful)
            response.body()?.let { if (it.metadata.totalCount > 0) return it }
        throw Exception("ошибка валидации ${(params as LoadCitiesParams).namePrefix}")
    }
}

class LoadWeatherRetrofitRequest(params: Params) :
    RetrofitRequest<WeatherResponsePOJO>(params) {
    override fun createService() = WeatherService.create()

    override suspend fun loadData(): Response<WeatherResponsePOJO> {
        params as LoadWeatherParams
        return (service as WeatherService).loadWeather(
            params.lat, params.lon, params.exclude,
            params.apiKey, params.units, params.lang
        )
    }

    override fun validate(response: Response<WeatherResponsePOJO>): WeatherResponsePOJO {
        if (response.isSuccessful) response.body()?.let { return it }
        throw Exception("ошибка валидации")
    }
}