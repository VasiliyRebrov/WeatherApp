package com.domain

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.data.common.Result
import com.data.model.City
import com.data.repo.AddCityRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class FindCityByNameUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : UseCase<String, List<City>>(coroutineDispatcher) {
    override suspend fun execute(parameters: String): List<City> {
        return repo.fetchCitiesByName(parameters)
    }
}

//class FindCityByNameUseCase(
//    private val repo: AddCityRepo,
//    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
//) : FlowUseCase<String, List<City>>(coroutineDispatcher) {
//    override fun execute(parameters: String): Flow<Result<List<City>>> {
//        return flow {
//            emit(Result.Loading)
//            kotlinx.coroutines.delay(500)
//            emit(Result.Error(Exception()))
//        }
//    }
//}

//class FindCityByNameUseCase(val repo: AddCityRepo, coroutineDispatcher: CoroutineDispatcher) :
//    FlowUseCase<String, List<City>>(coroutineDispatcher) {
//    override fun execute(parameters: String): Flow<Result<List<City>>> {
//        return repo.fetchCitiesByName(parameters)
//    }
//}

//class AddCityUseCase(
//    private val repo: AddCityRepo,
//    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
//) : UseCase<City, Int>(coroutineDispatcher) {
//    override suspend fun execute(parameters: City) = repo.addCity(parameters)
//}

class AddCityUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<City, Int>(coroutineDispatcher) {
    override fun execute(parameters: City): Flow<Result<Int>> {
        return repo.addCity(parameters)
    }
}

class DefineLocationUseCase(
    private val repo: AddCityRepo,
    private val locManager: LocationManager,
    private val locListener: LocationListener,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, Unit>(coroutineDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<Unit>> {
        return repo.defineLocation(locManager, locListener)
    }
}

class AddCityByLocationUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Location, Int>(coroutineDispatcher) {
    override fun execute(parameters: Location): Flow<Result<Int>> {
        return repo.addCityByLocation(parameters)
    }
}
//class AddNearCityByLocationUseCase(
//    private val repo: AddCityRepo,
//    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
//) : UseCase<Location, Int>(coroutineDispatcher) {
//    override suspend fun execute(parameters: Location) = repo.addNearCity(parameters)
//}
//
