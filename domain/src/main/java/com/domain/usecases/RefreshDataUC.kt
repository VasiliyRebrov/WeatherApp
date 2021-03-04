package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

data class RefreshWeatherParams(
    val unitMeasurePref: String,
    val lang: String,
    val newCities: List<City>,
    val oldCities: List<City> = listOf()
)

/**
 * [RefreshDataUC] обновляет погодные данные
 * */
class RefreshDataUC(
    private val repo: BaseRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<RefreshWeatherParams, String>(coroutineDispatcher) {
    override fun execute(params: RefreshWeatherParams): Flow<Result<String>> =
        repo.refreshData(
            params.newCities,
            params.oldCities,
            params.unitMeasurePref,
            params.lang
        )
}
