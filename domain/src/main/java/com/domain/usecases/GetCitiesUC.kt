package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [GetCitiesUC] возвращает [Flow], дающий список локальных городов
 * */
class GetCitiesUC(
    private val repo: BaseRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<Unit, List<City>>(coroutineDispatcher) {

    override fun execute(params: Unit): Flow<Result<List<City>>> = repo.localCities
}

