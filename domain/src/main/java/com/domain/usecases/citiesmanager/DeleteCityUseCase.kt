package com.domain.usecases.citiesmanager

import com.data.common.Result
import com.data.model.City
import com.data.repo.CitiesManagerRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class DeleteCityUseCase(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<City, Int>(coroutineDispatcher) {
    override fun execute(parameters: City): Flow<Result<Int>> {
        return repo.deleteCity(parameters)
    }
}
