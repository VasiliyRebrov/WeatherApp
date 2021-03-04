package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.CitiesManagerRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [ReorderCitiesUC] делает пересортировку локальных городов, путем редактирования их полей [City.position]
 * Возвращает [Int] количество редактированных городов
 * */
class ReorderCitiesUC(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<List<City>, Int>(coroutineDispatcher) {
    override fun execute(params: List<City>): Flow<Result<Int>> = repo.reorderCities(params)
}