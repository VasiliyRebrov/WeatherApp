package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [GetCitiesUseCase] возвращает [Flow], дающий список локальных городов
 * */
class GetCitiesUseCase(
    private val repo: BaseRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<Unit, List<City>>(coroutineDispatcher) {

    override fun execute(params: Unit): Flow<Result<List<City>>> = repo.localCities
}

