package com.weather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.data.common.Result
import com.data.model.*
import com.data.repo.CityItemRepo
import com.domain.RefreshWeatherParams
import com.domain.usecases.*
import com.weather.common.entities.Config

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

    val currentLD = Transformations.map(weatherDataLD) {
        Log.d("fffLD",it.toString())
        return@map (
                if (it is Result.Success)
                    Result.Success(it.data.currentWeatherData)
                else it
                ) as Result<CurrentWeatherData>
    }

    val hourlyLD = Transformations.map(weatherDataLD) {
        Log.d("qwerty2", it.toString())
        return@map (
                if (it is Result.Success)
                    Result.Success(it.data.hourlyList)
                else
                    it
                )
                as Result<List<Hourly>>
    }
    val dailyLD = Transformations.map(weatherDataLD) {
        Log.d("qwerty2", it.toString())
        return@map (
                if (it is Result.Success)
                    Result.Success(it.data.dailyList)
                else
                    it
                )
                as Result<List<Daily>>
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
        put(getWeatherDataUseCase.javaClass.simpleName, weatherDataLD as LiveData<Result<*>>)

        put(refreshWeatherDataUseCase.javaClass.simpleName, refreshDataLD as LiveData<Result<*>>)
    }
}