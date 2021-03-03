package com.data.remote.api.requests

import com.data.remote.api.RequestParams
import com.data.remote.api.services.WeatherService
import com.data.remote.entity.WeatherResponsePOJO
import retrofit2.Response
import java.lang.Exception

class LoadWeatherRetrofitRequest(override val params: RequestParams.WeatherRequestParams) :
    AbstractRetrofitRequest<WeatherResponsePOJO>() {
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