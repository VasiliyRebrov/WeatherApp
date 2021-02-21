package com.data.repo

import android.content.Context
import com.data.common.Result
import com.data.model.City
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class CitiesManagerRepo(ctx: Context) : BaseRepo(ctx) {
    val cityCurrentWeatherRelationList =
        dao.getFlowCityCurrentWeatherRelationList().map { Result.Success(it) }

    fun deleteCity(city: City) = flow {
        emit(Result.Loading)
        dao.deleteCity(city)
        if (dao.getCityList().isEmpty()) refreshShared(false)
        emit(Result.Success(city.cityId))
    }

    fun reorderLocalCities(cities: List<City>) = flow {
        emit(Result.Loading)
        val ids = dao.reorderLocalCities(*cities.toTypedArray())
        emit(Result.Success(ids.size))
    }
}