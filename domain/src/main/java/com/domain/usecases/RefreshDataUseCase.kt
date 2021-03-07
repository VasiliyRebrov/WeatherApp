package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

data class RefreshWeatherUseCaseParams(
    val unitMeasurePref: String,
    val lang: String,
    val newCities: List<City>,
    val oldCities: List<City> = listOf()
)

/**
 * [RefreshDataUseCase] обновляет погодные данные
 * */
class RefreshDataUseCase(
    private val repo: BaseRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<RefreshWeatherUseCaseParams, String>(coroutineDispatcher) {
    override fun execute(params: RefreshWeatherUseCaseParams): Flow<Result<String>> =
        repo.refreshData(
            params.newCities,
            params.oldCities,
            params.unitMeasurePref,
            params.lang
        )
}
