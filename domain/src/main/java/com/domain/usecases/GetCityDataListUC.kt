package com.domain.usecases

import com.data.common.Result
import com.data.model.CityData
import com.data.repo.CitiesManagerRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [GetCityDataListUC] возвращает [Flow], дающий список локальных городов и их погодные данные
 * */
class GetCityDataListUC(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<Unit, List<CityData>>(coroutineDispatcher) {
    override fun execute(params: Unit): Flow<Result<List<CityData>>> =
        repo.cityDataList
}