package com.domain.usecases

import com.data.model.City
import com.data.repo.AddCityRepo
import com.domain.usecases.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class FindCityByNameUCParams(
    val name: String,
    val lang: String
)

/**
 * [FindCityByNameUC] возвращает список городов найденных по названию
 * */
class FindCityByNameUC(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<FindCityByNameUCParams, List<City>>(coroutineDispatcher) {
    override suspend fun execute(params: FindCityByNameUCParams): List<City> =
        repo.loadCitiesByName(params.name, params.lang)
}





