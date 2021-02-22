package com.data.repo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.data.common.*
import com.data.model.*
import com.data.remote.api.*
import com.data.remote.entity.WeatherResponsePOJO
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

open class BaseRepo(protected val ctx: Context) {
    protected val dao = AppDataBase.getInstance(ctx).weatherDao()
    val localCities = dao.getFlowCities().map { Result.Success(it) }

    fun refreshData(unitMeasurePref: String, newCities: List<City>, oldCities: List<City>) =
        flow {
            emit(Result.Loading)
            val resultString = StringBuilder()
            coroutineScope {
                if (newCities.isNotEmpty()) {
                    async {
                        with(loadData(newCities, unitMeasurePref)) {
                            dao.insertActualData(*this.toTypedArray())
                            resultString.append("added: ${this.size} elements\n")
                        }
                    }.start()
                }
                if (oldCities.isNotEmpty()) {
                    async {
                        dao.deleteWeatherData(*oldCities.map { it.cityId }.toIntArray())
                        resultString.append("deleted: ${oldCities.size} elements\n")
                    }.start()
                }
            }
            emit(Result.Success(resultString.toString()))
        }

    private suspend fun loadData(cities: List<City>, unitMeasurePref: String) =
        coroutineScope {
            return@coroutineScope executeIfConnected {
                cities.map { city ->
                    async {
                        val result =
                            executeRequest(
                                Params.WeatherParams.createParams(
                                    city,
                                    unitMeasurePref
                                )
                            ) as WeatherResponsePOJO
                        createWeatherEntity(city.cityId, result)
                    }
                }.awaitAll()
            }
        }

    protected suspend fun <T> executeIfConnected(action: suspend () -> T): T {
        if (checkInternetAccess(ctx))
            return action()
        else
            throw NoNetworkException()
    }

    protected suspend fun executeRequest(params: Params) = when (params) {
        is Params.CitiesParams -> {
            LoadCitiesRetrofitRequest(params)
        }
        is Params.WeatherParams -> {
            LoadWeatherRetrofitRequest(params)
        }
    }.execute()

    protected fun switchLocalCitiesStatus(isExist: Boolean) {
        val sharedPref = ctx.getSharedPreferences("STORAGE", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isExistCitiesList", isExist)
            apply()
        }
    }
}