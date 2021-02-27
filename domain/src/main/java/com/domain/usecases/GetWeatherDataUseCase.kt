package com.domain.usecases

import android.util.Log
import com.data.common.Result
import com.data.model.WeatherData
import com.data.repo.CityItemRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class GetWeatherDataUseCase(
    private val repo: CityItemRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Int, WeatherData>(coroutineDispatcher) {
    override fun execute(params: Int): Flow<Result<WeatherData>> {
        return repo.getFlowWeatherDataByCity(params).map {
            if (it == null) {
                Log.d("qwerty0", "null")
                Result.Error(Exception("qwe"))
            } else {
                Log.d("qwerty0", "NOTNULL")
                Result.Success(it)
            }
        }
    }
}

