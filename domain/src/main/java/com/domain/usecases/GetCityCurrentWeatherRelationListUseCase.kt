package com.domain.usecases

import com.data.common.Result
import com.data.model.CityWeatherRelation
import com.data.repo.CitiesManagerRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class GetCityCurrentWeatherRelationListUseCase(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, List<CityWeatherRelation>>(coroutineDispatcher) {
    override fun execute(params: Unit): Flow<Result<List<CityWeatherRelation>>> {
        return repo.cityCurrentWeatherRelationList
    }
}