package com.data.remote.api.requests

import com.data.remote.api.Params
import com.data.remote.api.services.Service
import retrofit2.Response

abstract class AbstractRetrofitRequest<T> {
    protected abstract val params: Params
    protected abstract val service: Service
    suspend fun execute(): T {
        val response = loadData()
        return validate(response)
    }

    protected abstract suspend fun loadData(): Response<T>
    protected abstract fun validate(response: Response<T>): T
}