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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


class AddCityRepo(ctx: Context) : BaseRepo(ctx) {
    suspend fun fetchCitiesByName(name: String) =
        if (checkInternetAccess(ctx)) {
            (executeRequest(Params.CitiesParams.createParamsByName(name)) as CityResponse).data
        } else
            throw NoNetworkException()


    fun addCity(city: City): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        val localCities = dao.getCityList()
        localCities.firstOrNull { it.cityId == city.cityId }
            ?.let { throw CityAlreadyExistException() }
        city.position = localCities.size
        dao.insertCity(city)
        switchLocalCitiesStatus(true)
        kotlinx.coroutines.delay(1000)
        emit(Result.Success(city.cityId))
    }

    fun addCityByLocation(loc: Location) = flow {
        emit(Result.Loading)
        if (checkInternetAccess(ctx)) {
            val result =
                (executeRequest(Params.CitiesParams.createParamsByLoc(loc)) as CityResponse).data[0]
            emitAll(addCity(result))
        } else
            throw NoNetworkException()
    }


    fun defineLocation(
        locManager: LocationManager,
        locListener: LocationListener
    ) = flow {
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