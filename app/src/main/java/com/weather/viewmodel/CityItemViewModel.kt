package com.weather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.data.common.Result
import com.data.model.*
import com.data.repo.CityItemRepo
import com.domain.usecases.*
import com.weather.common.entities.Config.Companion.getUnitMeasurePref

class CityItemViewModel(
    application: Application,
    private val city: City,
    repo: CityItemRepo
) : BaseViewModel(application) {


    /** Refresh data*/
    private val refreshDataUC by lazy { RefreshDataUC(repo) }
    private val _refreshDataUCLD = MutableLiveData<Result<String>>()
    val refreshDataUCLD: LiveData<Result<String>> = _refreshDataUCLD

    fun refreshData() {
        val params = RefreshWeatherParams(
            getApplication<Application>().getUnitMeasurePref(),
            currentLang,
            listOf(city)
        )
        launchUseCase(refreshDataUC, params) {
            _refreshDataUCLD.value = it
        }
    }

    /** Get local data*/
    private val getDataUC = GetDataUC(repo)
    private val _getDataUCLD = getDataUC(city.cityId).asLiveData()
    val getDataUCLD: LiveData<Result<WeatherData>> = _getDataUCLD

    val currentLD = Transformations.map(getDataUCLD) {
        return@map (
                if (it is Result.Success)
                    Result.Success(it.data.currentWeather)
                else it
                ) as Result<CurrentWeather>
    }

    val hourlyLD = Transformations.map(getDataUCLD) {
        return@map (
                if (it is Result.Success)
                    Result.Success(it.data.hourlyList)
                else
                    it
                )
                as Result<List<HourlyWeather>>
    }
    val dailyLD = Transformations.map(getDataUCLD) {
        return@map (
                if (it is Result.Success)
                    Result.Success(it.data.dailyList)
                else
                    it
                )
                as Result<List<DailyWeather>>
    }

    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(getDataUC.javaClass.simpleName, getDataUCLD)
        put(refreshDataUC.javaClass.simpleName, refreshDataUCLD)
    } as Map<String, LiveData<Result<*>>>

}