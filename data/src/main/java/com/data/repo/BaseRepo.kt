package com.data.repo

import android.content.Context
import com.data.common.*
import com.data.model.*
import com.data.remote.api.*
import com.data.remote.api.requests.LoadCitiesRetrofitRequest
import com.data.remote.api.requests.LoadWeatherRetrofitRequest
import com.data.remote.entity.WeatherResponsePOJO
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*

open class BaseRepo(protected val ctx: Context) {
    protected val dao = AppDataBase.getInstance(ctx).weatherDao()
    val localCities = dao.getFlowCities().map { Result.Success(it) }

    fun refreshData(
        newCities: List<City>,
        oldCities: List<City>,
        unitMeasurePref: String,
        lang: String
    ) = flow {
        emit(Result.Loading)
        val resultString = StringBuilder()
        coroutineScope {
            if (newCities.isNotEmpty()) {
                async {
                    with(loadData(newCities, unitMeasurePref, lang)) {
                        val count = dao.insertData(*this.toTypedArray()).size
                        resultString.append("added: $count elements\n")
                    }
                }.start()
            }
            if (oldCities.isNotEmpty()) {
                async {
                    val count = dao.deleteDataById(*oldCities.map { it.cityId }.toIntArray())
                    resultString.append("deleted: $count elements\n")
                }.start()
            }
        }
        emit(Result.Success(resultString.toString()))
    }

    private suspend fun loadData(newCities: List<City>, unitMeasurePref: String, lang: String) =
        coroutineScope {
            return@coroutineScope executeIfConnected {
                newCities.map { city ->
                    async {
                        val result =
                            executeRequest(
                                RequestParams.WeatherRequestParams.createParams(
                                    city,
                                    unitMeasurePref,
                                    lang
                                )
                            ) as WeatherResponsePOJO
                        ctx.mapRemoteDataToLocal(city.cityId, result)
                    }
                }.awaitAll()
            }
        }

    protected suspend fun <T> executeIfConnected(action: suspend () -> T): T =
        if (checkInternetAccess(ctx))
            action()
        else
            throw NoNetworkException()


    protected suspend fun executeRequest(params: RequestParams) = when (params) {
        is RequestParams.GeoRequestParams -> {
            LoadCitiesRetrofitRequest(params)
        }
        is RequestParams.WeatherRequestParams -> {
            LoadWeatherRetrofitRequest(params)
        }
    }.execute()

}