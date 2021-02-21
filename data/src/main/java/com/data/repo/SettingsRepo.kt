package com.data.repo

import android.content.Context
import com.data.common.Result
import com.data.common.reConfig
import kotlinx.coroutines.flow.flow

class SettingsRepo(ctx: Context) : BaseRepo(ctx) {
    // разбить на шаблонный метод (распаралелить)
    fun reConfig(unitMeasurePref: String = "Metric") = flow {
        emit(Result.Loading)
        val current = dao.getCurrent()
        val hourly = dao.getHourly()
        val daily = dao.getDaily()
        current.forEach { it.reConfig(unitMeasurePref) }
        hourly.forEach {
            it.hourlyList.forEach { hourly -> hourly.reConfig(unitMeasurePref) }
        }
        daily.forEach {
            it.dailyList.forEach { daily -> daily.reConfig(unitMeasurePref) }
        }
        dao.insertWeathe(current, hourly, daily)
        emit(Result.Success(Unit))
    }
}
