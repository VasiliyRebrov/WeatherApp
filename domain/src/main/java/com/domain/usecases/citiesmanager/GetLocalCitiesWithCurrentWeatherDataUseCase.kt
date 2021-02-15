package com.domain.usecases.citiesmanager

import com.data.common.Result
import com.data.model.City
import com.data.model.CityAndCurrentWeather
import com.data.repo.CitiesManagerRepo
import com.data.repo.MainRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLocalCitiesWithCurrentWeatherDataUseCase(
    private val repo: CitiesManagerRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, List<CityAndCurrentWeather>>(coroutineDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<CityAndCurrentWeather>>> {
        /** полученный список городов сортируется по position.*/
        return repo.cityAndCurrentWeatherList.map { list ->
            val citiesWithDataList = list.sortedBy { it.city.position }
            Result.Success(citiesWithDataList)
        }
    }
}

//class GetLocalCitiesUseCase(
//    private val repo: MainRepo,
//    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
//) : FlowUseCase<Unit, List<City>>(coroutineDispatcher) {
//    override fun execute(parameters: Unit): Flow<Result<List<City>>> {
//        /** полученный список городов сортируется по id.*/
//        return repo.localCities.map {
//            val cities = it.sortedBy { city -> city.cityId }
//            Result.Success(cities)
//        }
//    }
//}
//
