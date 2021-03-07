package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.AddCityRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [AddCityUseCase] вызывается для добавления города.
 * Возвращает [Long] id внесенного города
 * */
class AddCityUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<City, Long>(coroutineDispatcher) {
    override fun execute(params: City): Flow<Result<Long>> = repo.addCity(params)
}

