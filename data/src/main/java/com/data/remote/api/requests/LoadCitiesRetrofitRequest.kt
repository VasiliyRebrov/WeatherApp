package com.data.remote.api.requests

import com.data.common.CitiesNotFoundException
import com.data.remote.api.Params
import com.data.remote.api.services.GeoService
import com.data.remote.entity.city.CityResponse
import retrofit2.Response
import java.lang.Exception

class LoadCitiesRetrofitRequest(override val params: Params.CitiesParams) :
    AbstractRetrofitRequest<CityResponse>() {
    override val service = GeoService.create()

    override suspend fun loadData(): Response<CityResponse> {
        return service.loadCities(
            namePrefix = params.namePrefix, location = params.location,
            radius = params.radius,
            distanceUnit = params.distanceUnit,
            languageCode = params.languageCode,
            limit = params.limit,
            sort = params.sort,
            types = params.types,
            hateoasMode = params.hateoasMode,
            api = params.api,
            apiKey = params.apiKey
        )
    }

    /**даже если вернулось 0 городов - результат HTTP считается 'OK' (успешным)*/
    override fun validate(response: Response<CityResponse>): CityResponse {
        if (response.isSuccessful)
            response.body()?.let { if (it.metadata.totalCount > 0) return it }
        /** В первом кейсе просто не найдены города по заданному параметру*/
        if (response.message() == "OK") throw CitiesNotFoundException()
        /** иначе, если не ОК, описать подробно ошибку*/
        else throw Exception("${response.message()} | ${response.errorBody()}")
    }
}
