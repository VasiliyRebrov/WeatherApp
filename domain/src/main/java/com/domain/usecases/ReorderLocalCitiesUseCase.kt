package com.domain.usecases

import com.data.common.Result
import com.data.common.data
import com.data.common.succeeded
import com.data.model.City
import com.data.repo.CitiesManagerRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReorderLocalCitiesUseCase(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<List<City>, Int>(coroutineDispatcher) {
    override fun execute(parameters: List<City>): Flow<Result<Int>> {
        return repo.reorderLocalCities(parameters)
    }
}