package com.domain.usecases

import com.data.common.Result
import com.data.model.CurrentWeatherData
import com.data.repo.CityItemRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class GetCurrentWeatherUseCase(
    private val repo: CityItemRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Int, CurrentWeatherData>(coroutineDispatcher) {
    override fun execute(params: Int): Flow<Result<CurrentWeatherData>> {
        return repo.getCurrentWeatherData(params)
    }
}