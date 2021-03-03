package com.domain.usecases

import com.data.common.Result
import com.data.model.City
import com.data.repo.CitiesManagerRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [DeleteCityUseCase]
 * Вызывается: Для удаления города.
 * Возвращает: [Int] id удаленного города.
 * */
class DeleteCityUseCase(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<City, Int>(coroutineDispatcher) {
    override fun execute(params: City): Flow<Result<Int>> {
        return repo.deleteCity(params)
    }
}

// в дальнейшем, возможно, сделать его, приримающим много городов на удалениеь