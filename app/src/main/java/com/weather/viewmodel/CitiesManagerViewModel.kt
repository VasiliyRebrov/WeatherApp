package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.data.common.Result
import com.data.model.City
import com.domain.usecases.DeleteCityUseCase
import com.domain.usecases.GetCityDataListUseCase
//import com.domain.usecases.GetCityCurrentWeatherRelationListUseCase
import com.domain.usecases.ReorderCitiesUseCase
import kotlinx.coroutines.flow.map


class CitiesManagerViewModel(application: Application, private val repo: CitiesManagerRepo) :
    BaseViewModel(application) {

    /** Get city with data list use case*/
    val getCityDataListUseCase = GetCityDataListUseCase(repo)
    val getCityDataListUseCaseLD =
        getCityDataListUseCase(Unit).map { result ->
            return@map if (result is Result.Success)
                Result.Success(result.data.sortedBy { it.city.position })
            else result
        }.asLiveData()

    /** Delete city use case*/
    private val deleteCityUseCase = DeleteCityUseCase(repo)
    private val _deleteCityUseCaseLD = MutableLiveData<Result<Int>>()
    val deleteCityUseCaseLD: LiveData<Result<Int>> = _deleteCityUseCaseLD

    fun deleteCity(city: City) {
        launchUseCase(deleteCityUseCase, city) {
            _deleteCityUseCaseLD.value = it
        }
    }

    /** Reorder cities use case*/
    private val reorderCitiesUseCase = ReorderCitiesUseCase(repo)
    private val _reorderCitiesUseCaseLD = MutableLiveData<Result<Int>>()
    val reorderCitiesUseCaseLD: LiveData<Result<Int>> = _reorderCitiesUseCaseLD


    fun reorderCities(reorderedCities: List<City>) {
        launchUseCase(reorderCitiesUseCase, reorderedCities) {
            _reorderCitiesUseCaseLD.value = it
        }
    }

    /** Others*/
    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(getCityDataListUseCase.javaClass.simpleName, getCityDataListUseCaseLD)
        put(deleteCityUseCase.javaClass.simpleName, deleteCityUseCaseLD)
        put(reorderCitiesUseCase.javaClass.simpleName, reorderCitiesUseCaseLD)
    } as Map<String, LiveData<Result<*>>>
}