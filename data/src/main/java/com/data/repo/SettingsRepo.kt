package com.data.repo

import android.content.Context
import android.util.Log
import com.data.common.Result
import com.data.common.transformData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import java.util.*

class SettingsRepo(ctx: Context) : BaseRepo(ctx) {
    fun transformData(unitMeasurePref: String) = flow {
        emit(Result.Loading)
        coroutineScope {
            val currentDeferred = async {
                with(dao.getCurrentWeather()) {
                    forEach { it.transformData(unitMeasurePref) }
                    return@async this
                }
            }
            val hourlyDeferred = async {
                with(dao.getHourlyWeather()) {
                    forEach {
                        it.hourlyList.forEach { hourly -> hourly.transformData(unitMeasurePref) }
                    }
                    return@async this
                }
            }

            val dailyDeferred = async {
                with(dao.getDailyWeather()) {
                    forEach {
                        it.dailyList.forEach { daily -> daily.transformData(unitMeasurePref) }
                    }
                    return@async this
                }
            }
            dao.insertTransformedData(
                currentDeferred.await(),
                hourlyDeferred.await(),
                dailyDeferred.await()
            )
        }
        emit(Result.Success(Unit))
    }
}