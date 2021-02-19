package com.data.repo

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.data.common.*
import com.data.model.City
import com.data.remote.api.LoadCitiesParams
import com.data.remote.api.LoadCitiesRetrofitRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.lang.Exception


class AddCityRepo(ctx: Context) : BaseRepo(ctx) {

    suspend fun fetchCitiesByName(name: String) =
        if (checkInternetAccess(ctx)) {
            val params = LoadCitiesParams.createParamsByName(name)
            val retrofitRequest = LoadCitiesRetrofitRequest(params)
            retrofitRequest.execute().data
        } else
            throw NoNetworkException()


    fun addCity(city: City): Flow<Result<Int>> = flow {
        emit(Result.Loading)
        val localCities = dao.getCityList()
        localCities.firstOrNull { it.cityId == city.cityId }
            ?.let { throw CityAlreadyExistException() }
        city.position = localCities.size
        dao.insertCity(city)
        emit(Result.Success(city.cityId))
    }

    fun addCityByLocation(location: Location) = flow {
        emit(Result.Loading)
        if (checkInternetAccess(ctx)) {
            val params = LoadCitiesParams.createParamsByLocation(location)
            val retrofitRequest = LoadCitiesRetrofitRequest(params)
            val nearCity = retrofitRequest.execute().data[0]
            emitAll(addCity(nearCity))
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