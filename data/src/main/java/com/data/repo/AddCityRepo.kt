package com.data.repo

import com.data.model.City
import com.data.remote.api.LoadCitiesParams
import com.data.remote.api.LoadCitiesRetrofitRequest
import com.data.remote.entity.city.CityResponse

class AddCityRepo {
    suspend fun fetchCitiesByName(name: String): List<City> {
        val params = LoadCitiesParams.createParamsByName(name)
        val retrofitRequest = LoadCitiesRetrofitRequest(params)
        return retrofitRequest.execute().data
    }

//    suspend fun findCities(inputText: String): AddCityUseCases {
//        if (inputText.isNotEmpty())
//            if (checkInternetAccess(ctx)) loadRemoteCities(
//                CitiesQuery.createParamsByName(
//                    inputText
//                )
//            )
//            else reset(NoNetworkException())
//        else reset(EmptyArgsException())
//        return AddCityUseCases.FINDCITY
//    }
}