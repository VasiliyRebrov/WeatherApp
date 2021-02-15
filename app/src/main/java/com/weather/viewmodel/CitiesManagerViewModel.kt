package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.data.common.Result
import com.data.model.City
import com.domain.usecases.citiesmanager.DeleteCityUseCase
import com.domain.usecases.citiesmanager.ReorderLocalCitiesUseCase


class CitiesManagerViewModel(application: Application, private val repo: CitiesManagerRepo) :
    BaseViewModel(application, repo) {

    private val getLocalCitiesWithCurrentWeatherDataUseCase=GetL

//    private val localCitiesAndWeatherData

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
        add(_deleteCityUseCaseLD)
        add(_reorderLocalCitiesUseCaseLD)
    } as Set<LiveData<Result<*>>>
}