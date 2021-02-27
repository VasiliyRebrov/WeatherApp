package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.data.common.Result
import com.data.model.City
import com.data.model.CityWeatherRelation
import com.domain.usecases.DeleteCityUseCase
import com.domain.usecases.GetCityCurrentWeatherRelationListUseCase
//import com.domain.usecases.GetCityCurrentWeatherRelationListUseCase
import com.domain.usecases.ReorderCitiesUseCase
import com.weather.components.CityCard
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt


class CitiesManagerViewModel(application: Application, private val repo: CitiesManagerRepo) :
    BaseViewModel(application) {

    val getCityCurrentWeatherRelationListUseCase = GetCityCurrentWeatherRelationListUseCase(repo)

    val cityCurrentWeatherRelationListLiveData =
        getCityCurrentWeatherRelationListUseCase(Unit).map { result ->
            return@map (if (result is Result.Success) {
                val sortedList = result.data.sortedBy { it.city.position }
                transformToCityCard(sortedList)
            } else result) as Result<List<CityCard>>
        }.asLiveData()

    private fun transformToCityCard(sortedList: List<CityWeatherRelation>) = Result.Success(
        sortedList.map {
            val temp = it.weatherData?.currentWeatherData?.temp?.roundToInt()?.toString() ?: "--"
            val icon = it.weatherData?.currentWeatherData?.icon ?: "a50n"
            val resId = with(getApplication<Application>()) {
                resources.getIdentifier(icon, "drawable", packageName)
            }
            CityCard(it.city, temp, resId)
        }
    )


    private val deleteCityUseCase = DeleteCityUseCase(repo)
    private val _deleteCityUseCaseLD = MutableLiveData<Result<Int>>()
    val deleteCityUseCaseLD: LiveData<Result<Int>> = _deleteCityUseCaseLD

    private val reorderLocalCitiesUseCase = ReorderCitiesUseCase(repo)
    private val _reorderLocalCitiesUseCaseLD = MutableLiveData<Result<Int>>()
    val reorderLocalCitiesUseCaseLD: LiveData<Result<Int>> = _reorderLocalCitiesUseCaseLD


    fun deleteCity(city: City) {
        launchUseCase(deleteCityUseCase, city) {
            _deleteCityUseCaseLD.value = it
        }
    }

    fun reorderLocalCities(reorderedCities: List<City>) {
        launchUseCase(reorderLocalCitiesUseCase, reorderedCities) {
            _reorderLocalCitiesUseCaseLD.value = it
        }
    }
//
//    override fun initLiveDataContainer() = mutableSetOf<LiveData<*>>().apply {
//        add(cityCurrentWeatherRelationListLiveData)
//        add(deleteCityUseCaseLD)
//        add(reorderLocalCitiesUseCaseLD)
//    }

    override fun initLiveDataContainer() = mutableMapOf<String, LiveData<Result<*>>>().apply {
        put(
            getCityCurrentWeatherRelationListUseCase.javaClass.simpleName,
            cityCurrentWeatherRelationListLiveData as LiveData<Result<*>>
        )
        put(deleteCityUseCase.javaClass.simpleName, deleteCityUseCaseLD as LiveData<Result<*>>)
        put(
            reorderLocalCitiesUseCase.javaClass.simpleName,
            reorderLocalCitiesUseCaseLD as LiveData<Result<*>>
        )
    }
}