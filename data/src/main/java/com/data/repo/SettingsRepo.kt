package com.data.repo

import android.content.Context
import android.util.Log
import com.data.common.Result
import com.data.common.reConfig
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import java.util.*

class SettingsRepo(ctx: Context) : BaseRepo(ctx) {
    // разбить на шаблонный метод (распаралелить)
//    fun reConfig(unitMeasurePref: String) = flow {
//        val start = Date().time
//        emit(Result.Loading)
//        coroutineScope {
//            val currentDeferrent = async {
//                with(dao.getCurrent()) {
//                    map {
//                        async { it.reConfig(unitMeasurePref) }
//                    }.awaitAll()
//                    return@async this
//                }
//            }
//
//            val hourlyDeferred = async {
//                with(dao.getHourly()) {
//                    map {
//                        async {
//                            it.hourlyList.map { hourly ->
//                                async {
//                                    hourly.reConfig(unitMeasurePref)
//                                }
//                            }.awaitAll()
//                        }
//                    }.awaitAll()
//                    return@async this
//                }
//            }
//            val dailyDeferred = async {
//                with(dao.getDaily()) {
//                    map {
//                        async {
//                            it.dailyList.map { daily ->
//                                async {
//                                    daily.reConfig(unitMeasurePref)
//                                }
//                            }.awaitAll()
//                        }
//                    }.awaitAll()
//                    return@async this
//                }
//            }
//            dao.insertWeathe(
//                currentDeferrent.await(),
//                hourlyDeferred.await(),
//                dailyDeferred.await()
//            )
//        }
//        val end = Date().time
//        Log.d("reconfigTime", (end - start).toString())
//        emit(Result.Success(Unit))
//    }


    fun reConfig(unitMeasurePref: String) = flow {
        val start = Date().time
        emit(Result.Loading)
        coroutineScope {
            val currentDeferred = async {
                with(dao.getCurrent()) {
                    forEach { it.reConfig(unitMeasurePref) }
                    return@async this
                }
            }
            val hourlyDeferred = async {
                with(dao.getHourly()) {
                    forEach {
                        it.hourlyList.forEach { hourly -> hourly.reConfig(unitMeasurePref) }
                    }
                    return@async this
                }
            }

            val dailyDeferred = async {
                with(dao.getDaily()) {
                    forEach {
                        it.dailyList.forEach { daily -> daily.reConfig(unitMeasurePref) }
                    }
                    return@async this
                }
            }
            dao.insertWeathe(currentDeferred.await(), hourlyDeferred.await(), dailyDeferred.await())
        }
        val end = Date().time
        Log.d("reconfigTime", (end - start).toString())
        emit(Result.Success(Unit))
    }
//    fun reConfig(unitMeasurePref: String) = flow {
//        emit(Result.Loading)
//        val current = dao.getCurrent()
//        val hourly = dao.getHourly()
//        val daily = dao.getDaily()
//        current.forEach { it.reConfig(unitMeasurePref) }
//        hourly.forEach {
//            it.hourlyList.forEach { hourly -> hourly.reConfig(unitMeasurePref) }
//        }
//        daily.forEach {
//            it.dailyList.forEach { daily -> daily.reConfig(unitMeasurePref) }
//        }
//        dao.insertWeathe(current, hourly, daily)
//        emit(Result.Success(Unit))
//    }
}
