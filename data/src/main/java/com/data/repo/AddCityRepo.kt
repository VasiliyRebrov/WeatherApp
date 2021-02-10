package com.data.repo

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.data.common.*
import com.data.model.City
import com.data.remote.api.LoadCitiesParams
import com.data.remote.api.LoadCitiesRetrofitRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.lang.Exception


class AddCityRepo(ctx: Context) : BaseRepo(ctx) {

    suspend fun fetchCitiesByName(name: String): List<City> {
        if (checkInternetAccess(ctx)) {
            val params = LoadCitiesParams.createParamsByName(name)
            val retrofitRequest = LoadCitiesRetrofitRequest(params)
            return retrofitRequest.execute().data
        } else
            throw NoNetworkException()
    }

    fun addCity(city: City): Flow<Result<Int>> {
        return flow {
            emit(Result.Loading)
            try {
                val localCities = dao.getCityList()
                localCities.firstOrNull { it.cityId == city.cityId }
                    ?.let { throw CityAlreadyExistException() }
                city.serialNumber = localCities.size
                dao.insertCity(city)
                emit(Result.Success(city.cityId))
            } catch (exc: Exception) {
                emit(Result.Error(exc))
            }
        }
    }

    fun addCityByLocation(location: Location): Flow<Result<Int>> {
        return flow {
            emit(Result.Loading)
            try {
                if (checkInternetAccess(ctx)) {
                    val params = LoadCitiesParams.createParamsByLocation(location)
                    val retrofitRequest = LoadCitiesRetrofitRequest(params)
                    val nearCity = retrofitRequest.execute().data[0]
                    emitAll(addCity(nearCity))
                } else
                    throw NoNetworkException()
            } catch (exc: Exception) {
                emit(Result.Error(exc))
            }
        }

    }

//    suspend fun addNearCity(location: Location): Int {
//        if (checkInternetAccess(ctx)) {
//            val params = LoadCitiesParams.createParamsByLocation(location)
//            val retrofitRequest = LoadCitiesRetrofitRequest(params)
//            val nearCity = retrofitRequest.execute().data[0]
//            return addCity(nearCity)
//        } else throw NoNetworkException()
//    }
//    //по 0 индексу идет ближайший город (проверить это утверждение, подставляя самому локацию)
//    suspend fun addNearCity(location: Location): AddCityUseCases {
//        if (checkInternetAccess(ctx))
//            addCity(
//                SingletonRetrofit.loadCities(CitiesQuery.createParamsByLocation(location))[0]
//            )
//        else throw NoNetworkException()
//        return AddCityUseCases.ADDCITY
//    }

    fun defineLocation(
        locManager: LocationManager,
        locListener: LocationListener
    ): Flow<Result<Unit>> {
        return flow {
            emit(Result.Loading)
            try {
                if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    if (ContextCompat.checkSelfPermission(
                            ctx,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //переключаемся на главный, чтобы работал слушатель
//                        withContext(Dispatchers.Main) {
                        locManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0,
                            0f,
                            locListener
                        )
//                        }
                    }
                    emit(Result.Success(Unit))
                } else
                    throw NetworkProviderDisabledException()
            } catch (exc: Exception) {
                emit(Result.Error(exc))
            }
        }
    }
}