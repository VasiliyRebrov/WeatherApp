package com.data.repo

import android.content.Context
import com.data.common.Config
import com.data.common.NoNetworkException
import com.data.common.Result
import com.data.common.checkInternetAccess
import com.data.common.createWeatherEntity
import com.data.model.*
import com.data.remote.api.LoadCitiesParams
import com.data.remote.api.LoadCitiesRetrofitRequest
import com.data.remote.api.LoadWeatherParams
import com.data.remote.api.LoadWeatherRetrofitRequest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.lang.StringBuilder

open class BaseRepo(protected val ctx: Context) {
    protected val dao = AppDataBase.getInstance(ctx).weatherDao()

    val localCities = dao.getFlowCityList().map { Result.Success(it) }

    fun refreshWeatherData(newCities: List<City>, oldCities: List<City>) = flow {
        emit(Result.Loading)
        val resultString = StringBuilder()
        if (newCities.isNotEmpty()) {
            val weatherData = loadWeatherData(newCities)
            dao.insertWeather(*weatherData.toTypedArray())
            resultString.append("added: ${newCities.size} elements\n")
        }
        if (oldCities.isNotEmpty()) {
            dao.deleteWeatherData(*oldCities.map { it.cityId }.toIntArray())
            resultString.append("deleted: ${oldCities.size} elements\n")
        }
        emit(Result.Success(resultString.toString()))
    }


    private suspend fun loadWeatherData(cities: List<City>): List<Triple<CurrentWeatherData, HourlyWeatherData, DailyWeatherData>> {
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

    suspend fun getLocalCities() = dao.getCityList()

}
