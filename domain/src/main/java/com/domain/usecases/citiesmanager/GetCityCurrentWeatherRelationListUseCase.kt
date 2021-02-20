package com.domain.usecases.citiesmanager

import com.data.common.Result
import com.data.model.CityCurrentWeatherRelation
import com.data.repo.CitiesManagerRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCityCurrentWeatherRelationListUseCase(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, List<CityCurrentWeatherRelation>>(coroutineDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<CityCurrentWeatherRelation>>> {
        /** полученный список городов сортируется по position.*/
        //убрать сортировку во viewmodel
        return repo.cityCurrentWeatherRelationList
    }
}