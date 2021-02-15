package com.data.repo

import android.content.Context
import android.database.SQLException
import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class CitiesManagerRepo(ctx: Context) : BaseRepo(ctx) {
    val cityAndCurrentWeatherList=dao.getItemsData()
    fun deleteCity(city: City) = flow {
        try {
            emit(Result.Loading)
            dao.deleteCity(city)
            emit(Result.Success(city.cityId))
        } catch (exc: Exception) {
            emit(Result.Error(exc))
        }
    }

    fun reorderLocalCities(cities: List<City>) = flow {
        try {
            emit(Result.Loading)
            val ids = dao.reorderLocalCities(*cities.toTypedArray())
            emit(Result.Success(ids.size))
        } catch (exc: Exception) {
            emit(Result.Error(exc))
        }
    }
}