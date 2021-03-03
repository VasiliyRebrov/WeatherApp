package com.data.repo

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.data.common.*
import com.data.model.City
import com.data.remote.api.RequestParams
import com.data.remote.entity.city.CityResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


class AddCityRepo(ctx: Context) : BaseRepo(ctx) {

    suspend fun loadCitiesByName(name: String, lang: String) = executeIfConnected {
        (executeRequest(RequestParams.GeoRequestParams.createParamsByName(name, lang)) as CityResponse).data
    }

    fun addCity(loc: Location, lang: String) = flow {
        emit(Result.Loading)
        executeIfConnected {
            val result =
                (executeRequest(
                    RequestParams.GeoRequestParams.createParamsByLoc(
                        loc,
                        lang
                    )
                ) as CityResponse).data[0]
            emitAll(addCity(result))
        }
    }

    //добавить проверку >10
    fun addCity(newCity: City) = flow {
        emit(Result.Loading)
        with(dao.getCities()) {
            firstOrNull { it.cityId == newCity.cityId }?.let { throw  CityAlreadyExistException() }
            newCity.position = size
        }
        val id = dao.insertCity(newCity)
        delay(300)  //убрать, когда будет подписка на shared.LD
        emit(Result.Success(id))
    }

    /**
     * Проверка [android.Manifest.permission.ACCESS_FINE_LOCATION] лежит в presentation-слое, в начале вызова
     * */
    @SuppressLint("MissingPermission")
    fun defineLocation(locManager: LocationManager, locListener: LocationListener) = flow {
        emit(Result.Loading)
        locManager.executeIfNetworkProviderEnabled {
            locManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0f,
                locListener
            )
            emit(Result.Success(Unit))
        }
    }

    private suspend fun <T> LocationManager.executeIfNetworkProviderEnabled(action: suspend () -> T) =
        if (this.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            action()
        else
            throw NetworkProviderDisabledException()
}