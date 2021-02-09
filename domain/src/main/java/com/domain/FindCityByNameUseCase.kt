package com.domain

import android.location.Location
import com.data.model.City
import com.data.repo.AddCityRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class FindCityByNameUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : UseCase<String, List<City>>(coroutineDispatcher) {
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

class AddCityUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : UseCase<City, Int>(coroutineDispatcher) {
    override suspend fun execute(parameters: City) = repo.addCity(parameters)
}

class AddNearCityByLocationUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : UseCase<Location, Int>(coroutineDispatcher) {
    override suspend fun execute(parameters: Location) = repo.addNearCity(parameters)
}

