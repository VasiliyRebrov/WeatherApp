package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import com.data.repo.MainRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class RefreshWeatherDataUseCase(
    private val repo: BaseRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Pair<List<City>, List<City>>, String>(coroutineDispatcher) {
    override fun execute(parameters: Pair<List<City>, List<City>>): Flow<Result<String>> {
        return repo.refreshWeatherData(parameters.first, parameters.second,)
    }
}
