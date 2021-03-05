package com.weather.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.lifecycle.*
import com.data.common.InvalidArgsException
import com.data.common.Result
import com.data.model.City
import com.data.repo.AddCityRepo
import com.domain.*
import com.domain.usecases.*
import kotlinx.coroutines.flow.*
import kotlin.Exception

class AddCityViewModel(application: Application, private val repo: AddCityRepo) :
    BaseViewModel(application) {
    private val locManager by lazy { application.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val locListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let { locManager.removeUpdates(this); addCity(it, currentLang) }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }



    /** Find city*/
    private val findCityByNameUC by lazy { FindCityByNameUC(repo) }
    private val _searchQuery = MutableStateFlow("")
    private val _searchResultLiveData = _searchQuery.asStateFlow().map {
        val result = try {
            //фильтровать пробелы
            if (it.length > 1) Result.Loading else throw InvalidArgsException()
        } catch (exc: Exception) {
            Result.Error(Exception(exc))
        }
        switchProgress(result)
        it
    }.debounce(1200)
        .filter { it.length > 1 }
        .mapLatest { name ->
            val params = FindCityByNameUCParams(name, currentLang)
            return@mapLatest findCityByNameUC(params)
        }.asLiveData()
    private val _findCityByNameUCLD = MediatorLiveData<Result<List<City>>>().apply {
        addSource(_searchResultLiveData) { this.value = it }
    }
    val findCityByNameUCLD: LiveData<Result<List<City>>> = _findCityByNameUCLD

    private fun switchProgress(result: Result<List<City>>) {
        _findCityByNameUCLD.value = result
    }

    fun search(name: String) {
        _searchQuery.value = name
    }

    fun retry() {
        with(_searchQuery) {
            val currentValue = value
            value = ""
            value = currentValue
        }
    }

    /** Add city*/
    private val addCityUC by lazy { AddCityUC(repo) }
    private val _addCityUCLD = MutableLiveData<Result<Long>>()
    val addCityUCLD: LiveData<Result<Long>> = _addCityUCLD

    fun addCity(city: City) {
        launchUseCase(addCityUC, city) { _addCityUCLD.value = it }
    }

    /** Add city by location*/
    private val addCityByLocUC by lazy { AddCityByLocUC(repo) }
    private val _addCityByLocUCLD = MutableLiveData<Result<Long>>()
    val addCityByLocUCLD: LiveData<Result<Long>> = _addCityByLocUCLD

    fun addCity(location: Location, lang: String) {
        val params = AddCityByLocUCParams(location, lang)
        launchUseCase(addCityByLocUC, params) { _addCityByLocUCLD.value = it }
    }

    /** Define Location*/
    private val defineLocUC by lazy { DefineLocUC(repo, locManager, locListener) }
    private val _defineLocUCLD = MutableLiveData<Result<Unit>>()
    val defineLocUCLD: LiveData<Result<Unit>> = _defineLocUCLD

    fun defineLoc() {
        launchUseCase(defineLocUC, Unit) { _defineLocUCLD.value = it }
    }

    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(findCityByNameUC.javaClass.simpleName, findCityByNameUCLD)
        put(defineLocUC.javaClass.simpleName, defineLocUCLD)
        put(addCityUC.javaClass.simpleName, addCityUCLD)
        put(addCityByLocUC.javaClass.simpleName, addCityByLocUCLD)
    } as Map<String, LiveData<Result<*>>>
}