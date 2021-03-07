package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.Result
import com.data.model.*
import com.data.repo.CityItemRepo
import com.domain.usecases.*
import com.weather.common.entities.Config.Companion.getUnitMeasurePref

class CityItemViewModel(application: Application, private val city: City, repo: CityItemRepo) :
    BaseViewModel(application) {
    /** Refresh data use case*/
    private val refreshDataUseCase = RefreshDataUseCase(repo)
    private val _refreshDataUseCaseLD = MutableLiveData<Result<String>>()
    val refreshDataUseCaseLD: LiveData<Result<String>> = _refreshDataUseCaseLD

    fun refreshData() {
        val params = RefreshWeatherUseCaseParams(
            getApplication<Application>().getUnitMeasurePref(),
            currentLang,
            listOf(city)
        )
        launchUseCase(refreshDataUseCase, params) {
            _refreshDataUseCaseLD.value = it
        }
    }

    /** Get local data use case*/
    private val getDataUseCase = GetDataUseCase(repo)
    private val getDataUseCaseLD = getDataUseCase(city.cityId).asLiveData()

    val currentLD = Transformations.map(getDataUseCaseLD) {
        return@map (
                if (it is Result.Success)
                    Result.Success(it.data.currentWeather)
                else it
                ) as Result<CurrentWeather>
    }

    val hourlyLD = Transformations.map(getDataUseCaseLD) {
        return@map (
                if (it is Result.Success)
                    Result.Success(it.data.hourlyList)
                else
                    it
                )
                as Result<List<HourlyWeather>>
    }
    val dailyLD = Transformations.map(getDataUseCaseLD) {
        return@map (
                if (it is Result.Success)
                    Result.Success(it.data.dailyList)
                else
                    it
                )
                as Result<List<DailyWeather>>
    }

    /** Others*/
    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(getDataUseCase.javaClass.simpleName, getDataUseCaseLD)
        put(refreshDataUseCase.javaClass.simpleName, refreshDataUseCaseLD)
    } as Map<String, LiveData<Result<*>>>

}