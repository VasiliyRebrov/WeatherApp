package com.data.repo

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.data.common.*
import com.data.model.City
import com.data.remote.api.Params
import com.data.remote.entity.city.CityResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


class AddCityRepo(ctx: Context) : BaseRepo(ctx) {
    suspend fun loadCitiesByName(name: String) = executeIfConnected {
        (executeRequest(Params.CitiesParams.createParamsByName(name)) as CityResponse).data
    }

    fun addCity(newCity: City): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        with(dao.getCities()) {
            firstOrNull { it.cityId == newCity.cityId }?.let { throw  CityAlreadyExistException() }
            newCity.position = size
        }
        val id = dao.insertCity(newCity)
        switchLocalCitiesStatus(true)
        delay(300)
        emit(Result.Success(id.toInt()))
    }


    fun addCityByLoc(loc: Location) = flow {
        emit(Result.Loading)
        executeIfConnected {
            val result =
                (executeRequest(Params.CitiesParams.createParamsByLoc(loc)) as CityResponse).data[0]
            emitAll(addCity(result))
        }
    }

    fun defineLocation(locManager: LocationManager, locListener: LocationListener) = flow {
        emit(Result.Loading)
        if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(
                    ctx,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    locListener
                )
            }
            emit(Result.Success(Unit))
        } else
            throw NetworkProviderDisabledException()
    }
}