package com.data.repo

import android.content.Context
import android.location.Location
import com.data.common.*
import com.data.model.City
import com.data.remote.api.LoadCitiesParams
import com.data.remote.api.LoadCitiesRetrofitRequest


class AddCityRepo(ctx: Context) : BaseRepo(ctx) {

    suspend fun fetchCitiesByName(name: String): List<City> {
        if (checkInternetAccess(ctx)) {
            val params = LoadCitiesParams.createParamsByName(name)
            val retrofitRequest = LoadCitiesRetrofitRequest(params)
            return retrofitRequest.execute().data
        } else
            throw NoNetworkException()
    }

    suspend fun addCity(city: City): Int {
        val localCities = dao.getCityList()
        localCities.firstOrNull { it.cityId == city.cityId }
            ?.let { throw CityAlreadyExistException() }
        city.serialNumber = localCities.size
        dao.insertCity(city)
        return city.cityId
    }

    suspend fun addNearCity(location: Location): Int {
        if (checkInternetAccess(ctx)) {
            val params = LoadCitiesParams.createParamsByLocation(location)
            val retrofitRequest = LoadCitiesRetrofitRequest(params)
            val nearCity = retrofitRequest.execute().data[0]
            return addCity(nearCity)
        } else throw NoNetworkException()
    }
//    //по 0 индексу идет ближайший город (проверить это утверждение, подставляя самому локацию)
//    suspend fun addNearCity(location: Location): AddCityUseCases {
//        if (checkInternetAccess(ctx))
//            addCity(
//                SingletonRetrofit.loadCities(CitiesQuery.createParamsByLocation(location))[0]
//            )
//        else throw NoNetworkException()
//        return AddCityUseCases.ADDCITY
//    }


}