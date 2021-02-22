package com.domain.usecases

import com.data.model.City
import com.data.repo.AddCityRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FindCityByNameUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : UseCase<String, List<City>>(coroutineDispatcher) {
    override suspend fun execute(parameters: String): List<City> {
        return repo.loadCitiesByName(parameters)
    }
}





