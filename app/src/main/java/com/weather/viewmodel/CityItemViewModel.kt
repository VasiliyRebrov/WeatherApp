package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.Result
import com.data.common.data
import com.data.common.succeeded
import com.data.model.City
import com.data.model.Hourly
import com.data.model.WeatherData
import com.data.repo.CityItemRepo
import com.domain.RefreshWeatherParams
import com.domain.usecases.*
import com.weather.components.Config
import kotlinx.coroutines.flow.map
import java.lang.Error

class CityItemViewModel(
    application: Application,
    private val city: City,
    repo: CityItemRepo
) : BaseViewModel(application) {
    private val refreshWeatherDataUseCase = RefreshWeatherDataUseCase(repo)
    private val _refreshDataLD = MutableLiveData<Result<String>>()
    val refreshDataLD: LiveData<Result<String>> = _refreshDataLD

    private val getWeatherDataUseCase = GetWeatherDataUseCase(repo)
    private val _weatherDataLD = getWeatherDataUseCase(city.cityId).asLiveData()
    val weatherDataLD: LiveData<Result<WeatherData>> = _weatherDataLD

    val hourlyLD = Transformations.map(weatherDataLD) {
        return@map (if (it is Result.Success) {
            Result.Success(it.data.hourlyList)
        } else it) as Result<List<Hourly>>
    }
    val dailyLD = Transformations.map(weatherDataLD) {
        return@map if (it is Result.Success) {
            Result.Success(it.data.dailyList)
        } else it
    }

    fun refreshWeatherData() {
        val params = RefreshWeatherParams(
            Config.getInstance(getApplication()).unitMeasurePref,
            listOf(city)
        )
        launchUseCase(refreshWeatherDataUseCase, params) {
            _refreshDataLD.value = it
        }
    }

    override fun initLiveDataContainer() = mutableMapOf<String, LiveData<Result<*>>>().apply {

        put(refreshWeatherDataUseCase.javaClass.simpleName, refreshDataLD as LiveData<Result<*>>)
    }
}