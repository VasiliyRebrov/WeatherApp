package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.data.common.Result
import com.data.model.City
import com.domain.usecases.DeleteCityUseCase
import com.domain.usecases.GetCityCurrentWeatherRelationListUseCase
import com.domain.usecases.ReorderCitiesUseCase
import kotlinx.coroutines.flow.map


class CitiesManagerViewModel(application: Application, private val repo: CitiesManagerRepo) :
    BaseViewModel(application, repo) {

    val getCityCurrentWeatherRelationListUseCase = GetCityCurrentWeatherRelationListUseCase(repo)

    val cityCurrentWeatherRelationListLiveData =
        getCityCurrentWeatherRelationListUseCase(Unit).map { result ->
            return@map if (result is Result.Success && result.data.isNotEmpty()) {
                Result.Success(result.data.sortedBy { it.city.position })
            } else result
        }.asLiveData()

    private val deleteCityUseCase = DeleteCityUseCase(repo)
    private val _deleteCityUseCaseLD = MutableLiveData<Result<Int>>()
    val deleteCityUseCaseLD: LiveData<Result<Int>> = _deleteCityUseCaseLD

    private val reorderLocalCitiesUseCase = ReorderCitiesUseCase(repo)
    private val _reorderLocalCitiesUseCaseLD = MutableLiveData<Result<Int>>()
    val reorderLocalCitiesUseCaseLD: LiveData<Result<Int>> = _reorderLocalCitiesUseCaseLD


    fun deleteCity(city: City) {
        launchUseCase(deleteCityUseCase, city) {
            //обработка удаления города
            _deleteCityUseCaseLD.value = it
        }
    }

    fun reorderLocalCities(reorderedCities: List<City>) {
        launchUseCase(reorderLocalCitiesUseCase, reorderedCities) {
            //обработка пересортировка городов
            _reorderLocalCitiesUseCaseLD.value = it
        }
    }

    override fun initLiveDataContainer() = mutableSetOf<LiveData<*>>().apply {
        add(deleteCityUseCaseLD)
        add(reorderLocalCitiesUseCaseLD)
    } as Set<LiveData<Result<*>>>

    override fun onCleared() {
        super.onCleared()
    }
}