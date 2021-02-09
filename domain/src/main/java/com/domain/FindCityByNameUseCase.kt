package com.domain

import com.data.model.City
import com.data.repo.AddCityRepo
import kotlinx.coroutines.CoroutineDispatcher

class FindCityByNameUseCase(val repo: AddCityRepo, coroutineDispatcher: CoroutineDispatcher) :
    UseCase<String, List<City>>(coroutineDispatcher) {
    override suspend fun execute(parameters: String): List<City> {
        return repo.fetchCitiesByName(parameters)
    }
}

//class FindCityByNameUseCase(val repo: AddCityRepo, coroutineDispatcher: CoroutineDispatcher) :
//    FlowUseCase<String, List<City>>(coroutineDispatcher) {
//    override fun execute(parameters: String): Flow<Result<List<City>>> {
//        return repo.fetchCitiesByName(parameters)
//    }
//}