package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class GetLocalCitiesUseCase(
    private val repo: BaseRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, List<City>>(coroutineDispatcher) {

    override fun execute(params: Unit): Flow<Result<List<City>>> {
        return repo.localCities
    }
}

