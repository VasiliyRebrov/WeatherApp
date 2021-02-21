package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.AddCityRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class AddCityUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<City, Int>(coroutineDispatcher) {
    override fun execute(parameters: City): Flow<Result<Int>> {
        return repo.addCity(parameters)
    }
}

