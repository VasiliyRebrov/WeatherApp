package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.data.common.Result
import com.data.model.City
import com.data.repo.CityItemRepo
import com.domain.RefreshWeatherParams
import com.domain.usecases.GetCurrentWeatherUseCase
import com.domain.usecases.RefreshWeatherDataUseCase
import com.weather.components.Config

class CityItemViewModel(
    application: Application,
    private val city: City,
    repo: CityItemRepo
) : BaseViewModel(application) {

    private val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(repo)
    val currentWeatherLD = getCurrentWeatherUseCase(city.cityId).asLiveData()

    private val refreshWeatherDataUseCase = RefreshWeatherDataUseCase(repo)
    private val _refreshDataLD = MutableLiveData<Result<String>>()
    val refreshDataLD: LiveData<Result<String>> = _refreshDataLD

    fun refreshWeatherData() {
        val params = RefreshWeatherParams(
            Config.getInstance(getApplication()).unitMeasurePref,
            listOf(city)
        )
        launchUseCase(refreshWeatherDataUseCase, params) {
            _refreshDataLD.value = it
        }
    }
//
//    override fun initLiveDataContainer() = mutableSetOf<LiveData<*>>().apply {
//        add(currentWeatherLD)
//        add(refreshDataLD)
//    }

    override fun initLiveDataContainer() = mutableMapOf<String, LiveData<Result<*>>>().apply {
        put(getCurrentWeatherUseCase.javaClass.simpleName, currentWeatherLD as LiveData<Result<*>>)
        put(refreshWeatherDataUseCase.javaClass.simpleName, refreshDataLD as LiveData<Result<*>>)

    }

}