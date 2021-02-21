package com.data.remote.api

import android.util.Log
import androidx.annotation.RestrictTo
import com.data.common.CitiesNotFoundException
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

abstract class RetrofitRequest<T> {
    protected abstract val params: Params
    protected abstract val service: Service
    suspend fun execute(): T {
        val response = loadData()
        return validate(response)
    }

    protected abstract suspend fun loadData(): Response<T>
    protected abstract fun validate(response: Response<T>): T
}

class LoadCitiesRetrofitRequest(override val params: LoadCitiesParams) :
    RetrofitRequest<CityResponse>() {
    override val service = GeoService.create()

    override suspend fun loadData(): Response<CityResponse> {
        return service.loadCities(
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

    /**даже если вернулось 0 городов - результат HTTP считается 'OK' (успешным)*/
    override fun validate(response: Response<CityResponse>): CityResponse {
        if (response.isSuccessful)
            response.body()?.let { if (it.metadata.totalCount > 0) return it }
        /** В первом кейсе просто не найдены города по заданному параметру*/
        if (response.message() == "OK") throw CitiesNotFoundException()
        /** иначе, если не ОК, описать подробно ошибку*/
        else throw Exception("${response.message()} | ${response.errorBody()}")
    }
}

class LoadWeatherRetrofitRequest(override val params: LoadWeatherParams) :
    RetrofitRequest<WeatherResponsePOJO>() {
    override val service = WeatherService.create()

    override suspend fun loadData(): Response<WeatherResponsePOJO> {
        return service.loadWeather(
            params.lat, params.lon, params.exclude,
            params.apiKey, params.units, params.lang
        )
    }

    override fun validate(response: Response<WeatherResponsePOJO>): WeatherResponsePOJO {
        if (response.isSuccessful) response.body()?.let { return it }
        throw Exception("${response.message()} | ${response.errorBody()}")
    }
}