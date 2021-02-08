package com.data.repo

import android.content.Context
import com.data.common.NoNetworkException
import com.data.common.Result
import com.data.common.checkInternetAccess
import com.data.model.City
import com.data.remote.api.LoadCitiesParams
import com.data.remote.api.LoadCitiesRetrofitRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AddCityRepo(private val ctx: Context) {
     fun fetchCitiesByName(name: String): Flow<Result<List<City>>> {
        return flow {
            emit(Result.Loading)
            if (checkInternetAccess(ctx)) {
                val params = LoadCitiesParams.createParamsByName(name)
                val retrofitRequest = LoadCitiesRetrofitRequest(params)
                val cities = retrofitRequest.execute().data
                emit(Result.Success(cities))
            } else
                throw NoNetworkException()
        }
    }

    suspend fun ffetchCitiesByName(name: String): List<City> {
        val params = LoadCitiesParams.createParamsByName(name)
        val retrofitRequest = LoadCitiesRetrofitRequest(params)
        return retrofitRequest.execute().data
    }
}