package com.domain.usecases

import com.data.common.Result
import com.data.model.WeatherData
import com.data.repo.CityItemRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class GetWeatherDataUseCase(
    private val repo: CityItemRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Int, WeatherData>(coroutineDispatcher) {
    override fun execute(params: Int): Flow<Result<WeatherData>> {
        return repo.getFlowWeatherDataByCity(params)
    }
}

