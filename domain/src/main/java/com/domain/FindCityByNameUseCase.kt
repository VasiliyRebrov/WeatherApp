package com.domain

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.data.common.Result
import com.data.common.succeeded
import com.data.model.City
import com.data.repo.AddCityRepo
import com.data.repo.MainRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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


class GetLocalCitiesUseCase(
    private val repo: MainRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, List<City>>(coroutineDispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<City>>> {
        /** полученный список городов сортируется по id.*/
        return repo.localCities.map {
            val cities = it.sortedBy { city -> city.cityId }
            Result.Success(cities)
        }
    }
}

class RefreshWeatherDataUseCase(
    private val repo: MainRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Pair<List<City>, List<City>>, String>(coroutineDispatcher) {
    override fun execute(parameters: Pair<List<City>, List<City>>): Flow<Result<String>> {
        return repo.refreshWeatherData(parameters.first, parameters.second)
    }

}

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
        /** используем маппинг, преобразуя успешный результат в результат загрузки
         * причина: когда успешно прекращается данный юзкейс, автоматически начинается другой - добавление города
         *но интервал между окончанием выполнения этого юзкейса и началом выполнения нового - он очень велик
         * потому что мы ждем, когда найдется локация и location listener начнет новый юзкейс
         * этот интервал ожидания должен сопровождаться загрузкой*/
        return repo.defineLocation(locManager, locListener).map { result ->
            return@map if (result.succeeded) Result.Loading
            else result
        }
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
