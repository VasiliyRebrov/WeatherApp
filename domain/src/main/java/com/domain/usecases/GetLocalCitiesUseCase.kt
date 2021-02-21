package com.domain.usecases

import android.util.Log
import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import com.data.repo.MainRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLocalCitiesUseCase(
    private val repo: BaseRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, List<City>>(coroutineDispatcher) {

    override fun execute(parameters: Unit): Flow<Result<List<City>>> {
        return repo.localCities
    }
}

