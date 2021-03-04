package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.CitiesManagerRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [DeleteCityUC] вызывается для удаления городов.
 * Возвращает [Int] количество удаленных городов.
 * (сейчас функционал рассчитан лишь на один, в будущем юз-кейс будет расчитан на множественное
 * удаление одной транзакцией и переименован в DeleteCitiesUC)
 * */
class DeleteCityUC(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<City, Int>(coroutineDispatcher) {
    override fun execute(params: City): Flow<Result<Int>> = repo.deleteCity(params)
}