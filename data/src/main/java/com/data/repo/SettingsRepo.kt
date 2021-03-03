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
        val transformedData = coroutineScope {
            dao.getData().map { data ->
                async {
                    data.currentWeather.transformData(unitMeasurePref)
                    data.hourlyList.forEach { hourly -> hourly.transformData(unitMeasurePref) }
                    data.dailyList.forEach { daily -> daily.transformData(unitMeasurePref) }
                    return@async data
                }
            }.awaitAll()
        }
        val count = dao.insertData(*transformedData.toTypedArray()).size
        emit(Result.Success(count))
    }
}