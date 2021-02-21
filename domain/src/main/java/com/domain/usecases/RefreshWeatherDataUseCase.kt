package com.domain.usecases

import com.data.common.Result
import com.data.repo.BaseRepo
import com.domain.RefreshWeatherParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class RefreshWeatherDataUseCase(
    private val repo: BaseRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<RefreshWeatherParams, String>(coroutineDispatcher) {
    override fun execute(params: RefreshWeatherParams): Flow<Result<String>> {
        return repo.refreshWeatherData(params.unitMeasurePref, params.newCities, params.oldCities)
    }
}
