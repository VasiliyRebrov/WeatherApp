package com.data.repo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.data.common.NoNetworkException
import com.data.common.Result
import com.data.common.checkInternetAccess
import com.data.common.createWeatherEntity
import com.data.model.*
import com.data.remote.api.*
import com.data.remote.entity.WeatherResponsePOJO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

open class BaseRepo(protected val ctx: Context) {
    protected val dao = AppDataBase.getInstance(ctx).weatherDao()

    val localCities = dao.getFlowCityList().map { Result.Success(it) }

    fun refreshWeatherData(
        newCities: List<City>,
        oldCities: List<City>,
        unitMeasurePref: String = "Metric"
    ) =
        flow {
            emit(Result.Loading)
            val start = Date()
            val resultString = StringBuilder()
            if (newCities.isNotEmpty()) {
                val weatherData = loadWeatherData(newCities, unitMeasurePref)
                dao.insertWeather(*weatherData.toTypedArray())
                resultString.append("added: ${newCities.size} elements\n")
            }
            if (oldCities.isNotEmpty()) {
                dao.deleteWeatherData(*oldCities.map { it.cityId }.toIntArray())
                resultString.append("deleted: ${oldCities.size} elements\n")
            }
            val end = Date()
            Log.d("checkTime", (end.time - start.time).toString())
            emit(Result.Success(resultString.toString()))
        }


    private suspend fun loadWeatherData(
        cities: List<City>,
        unitMeasurePref: String
    ): List<Triple<CurrentWeatherData, HourlyWeatherData, DailyWeatherData>> {
        return if (checkInternetAccess(ctx)) {
            cities.map { city ->
                val result =
                    executeRequest(
                        Params.WeatherParams.createParams(
                            city,
                            unitMeasurePref
                        )
                    ) as WeatherResponsePOJO
                createWeatherEntity(city.cityId, result)
            }
        } else throw NoNetworkException()
    }

    protected fun refreshShared(isExist: Boolean) {
        val sharedPref = ctx.getSharedPreferences("STORAGE", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isExistCitiesList", isExist)
            apply()
        }
    }

    suspend fun executeRequest(params: Params) = when (params) {
        is Params.CitiesParams -> {
            LoadCitiesRetrofitRequest(params)
        }
        is Params.WeatherParams -> {
            LoadWeatherRetrofitRequest(params)
        }
    }.execute()
}