package com.data.remote.api.requests

import com.data.remote.api.RequestParams
import com.data.remote.api.services.Service
import retrofit2.Response
// переопределить operator invoke()
abstract class AbstractRetrofitRequest<T> {
    protected abstract val params: RequestParams
    protected abstract val service: Service

    /** [execute] - шаблонный метод*/
    suspend fun execute(): T {
        val response = loadData()
        return validate(response)
    }

    protected abstract suspend fun loadData(): Response<T>
    protected abstract fun validate(response: Response<T>): T
}