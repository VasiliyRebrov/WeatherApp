package com.domain

import com.data.model.City
import com.data.repo.AddCityRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher

class FindCityByNameUseCase(val repo: AddCityRepo, coroutineDispatcher: CoroutineDispatcher) :
    UseCase<String, List<City>>(coroutineDispatcher) {
    override suspend fun execute(parameters: String): List<City> {
        return repo.fetchCitiesByName(parameters)
    }
}