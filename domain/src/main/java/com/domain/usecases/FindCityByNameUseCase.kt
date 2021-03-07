package com.domain.usecases

import com.data.model.City
import com.data.repo.AddCityRepo
import com.domain.usecases.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class FindCityByNameUseCaseParams(
    val name: String,
    val lang: String
)

/**
 * [FindCityByNameUseCase] возвращает список городов найденных по названию
 * */
class FindCityByNameUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<FindCityByNameUseCaseParams, List<City>>(coroutineDispatcher) {
    override suspend fun execute(params: FindCityByNameUseCaseParams): List<City> =
        repo.loadCitiesByName(params.name, params.lang)
}





