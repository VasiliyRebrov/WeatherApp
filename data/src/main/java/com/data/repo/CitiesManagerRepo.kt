package com.data.repo

import android.content.Context
import com.data.common.Result
import com.data.model.City
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CitiesManagerRepo(ctx: Context) : BaseRepo(ctx) {
    val cityCurrentWeatherRelationList =
        dao.getFlowCityCurrentWeatherRelationList().map { Result.Success(it) }

    fun deleteCity(city: City) = flow {
        emit(Result.Loading)
        val id = dao.deleteCity(city)
        if (dao.getCities().isEmpty()) switchLocalCitiesStatus(false)
        emit(Result.Success(id))
    }


    fun reorderCities(cities: List<City>) = flow {
        emit(Result.Loading)
        val count = dao.updateCities(*cities.toTypedArray())
        emit(Result.Success(count))
    }
}