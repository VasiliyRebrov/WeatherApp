package com.data.repo

import android.content.Context
import com.data.common.Result
import com.data.common.transformData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

class SettingsRepo(ctx: Context) : BaseRepo(ctx) {
    fun transformData(unitMeasurePref: String) = flow {
        emit(Result.Loading)
        val transformedWeatherData = coroutineScope {
            dao.getWeatherData().map { weatherData ->
                async {
                    weatherData.currentWeatherData.transformData(unitMeasurePref)
                    weatherData.hourlyList.forEach { hourly -> hourly.transformData(unitMeasurePref) }
                    weatherData.dailyList.forEach { daily -> daily.transformData(unitMeasurePref) }
                    return@async weatherData
                }
            }.awaitAll()
        }
        dao.insertActualWeatherData(*transformedWeatherData.toTypedArray())
//        val transformedWeatherDate = dao.getWeatherData().onEach { weatherData ->
//            weatherData.currentWeatherData.transformData(unitMeasurePref)
//            weatherData.hourlyList.forEach { hourly -> hourly.transformData(unitMeasurePref) }
//            weatherData.dailyList.forEach { daily -> daily.transformData(unitMeasurePref) }
//        }
//        dao.insertActualWeatherData(*transformedWeatherDate.toTypedArray())
        emit(Result.Success(Unit))
    }
}