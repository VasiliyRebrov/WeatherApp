package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.data.common.Result
import com.data.common.data
import com.data.model.City
import com.data.model.CityCurrentWeatherRelation
import com.domain.usecases.citiesmanager.DeleteCityUseCase
import com.domain.usecases.citiesmanager.GetCityCurrentWeatherRelationListUseCase
import com.domain.usecases.citiesmanager.ReorderLocalCitiesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn


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

    private val reorderLocalCitiesUseCase = ReorderLocalCitiesUseCase(repo)
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