package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.CitiesManagerRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [ReorderCitiesUseCase]
 * Вызывается: Для пересортировки городов.
 * Возвращает: [Int] количество пересортированных городов
 * */
class ReorderCitiesUseCase(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<List<City>, Int>(coroutineDispatcher) {
    override fun execute(params: List<City>): Flow<Result<Int>> {
        return repo.reorderCities(params)
    }
}