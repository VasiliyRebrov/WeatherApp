package com.data.repo

import android.content.Context
import com.data.common.Config
import com.data.common.Result
import com.data.common.reConfig
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

class SettingsRepo(ctx: Context) : BaseRepo(ctx) {
    // разбить на шаблонный метод (распаралелить)
     fun reConfig() = flow {
        emit(Result.Loading)
        val config = Config.getInstance(ctx).unitsPref
        val current = dao.getCurrent()
        val hourly = dao.getHourly()
        val daily = dao.getDaily()
        current.forEach { it.reConfig(config) }
        hourly.forEach {
            it.hourlyList.forEach { hourly -> hourly.reConfig(config) }
        }
        daily.forEach {
            it.dailyList.forEach { daily -> daily.reConfig(config) }
        }
        dao.insertWeathe(current, hourly, daily)
        emit(Result.Success(Unit))
    }

}
