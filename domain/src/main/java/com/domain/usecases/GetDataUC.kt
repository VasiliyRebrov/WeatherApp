package com.domain.usecases

import com.data.common.Result
import com.data.model.WeatherData
import com.data.repo.CityItemRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

/**
 * [GetDataUC] возвращает [Flow], дающий список локальных погодных данных
 * */
class GetDataUC(
    private val repo: CityItemRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<Int, WeatherData>(coroutineDispatcher) {
    override fun execute(params: Int): Flow<Result<WeatherData>> =
        repo.getFlowWeatherDataByCity(params).map {
            if (it != null)
                Result.Success(it)
            else
                Result.Error(Exception("qwe"))
        }
}

