package com.data.repo

import android.content.Context
import com.data.common.Config
import com.data.common.NoNetworkException
import com.data.common.checkInternetAccess
import com.data.common.createWeatherEntity
import com.data.model.*
import com.data.remote.api.LoadCitiesParams
import com.data.remote.api.LoadCitiesRetrofitRequest
import com.data.remote.api.LoadWeatherParams
import com.data.remote.api.LoadWeatherRetrofitRequest

open class BaseRepo(protected val ctx: Context) {
    protected val dao = AppDataBase.getInstance(ctx).weatherDao()

    suspend fun loadWeatherData(cities: List<City>): List<Triple<CurrentWeatherData, HourlyWeatherData, DailyWeatherData>> {
        val units = Config.getInstance(ctx).unitsPref
        return if (checkInternetAccess(ctx)) {
            cities.map { city ->
                val params =
                    LoadWeatherParams(lat = city.latitude, lon = city.longitude, units = units)
                val retrofitRequest = LoadWeatherRetrofitRequest(params)
                val remoteWeather = retrofitRequest.execute()
                createWeatherEntity(city.cityId, remoteWeather)
            }
        } else throw NoNetworkException()
    }
}
