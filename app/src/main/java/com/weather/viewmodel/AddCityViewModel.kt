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
    private val locManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let { locManager.removeUpdates(this); addCity(it, currentLang) }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    /** Find city by name use case*/
    private val findCityByNameUseCase = FindCityByNameUseCase(repo)

    /**[Flow] принимает и обрабатывает значения, поступающие с [EditText]*/
    private val _searchQuery = MutableStateFlow("")
    private val _searchResultLiveData = _searchQuery.asStateFlow().map {
        val result = try {
            //фильтровать пробелы
            if (it.length > 1)
                Result.Loading
            else
                throw InvalidArgsException()
        } catch (exc: Exception) {
            Result.Error(Exception(exc))
        }
        switchProgress(result)
        it
    }.debounce(1200)
        .filter { it.length > 1 }
        .mapLatest { name ->
            val params = FindCityByNameUseCaseParams(name, currentLang)
            return@mapLatest findCityByNameUseCase(params)
        }.asLiveData()

    private val _findCityByNameUseCaseLD = MediatorLiveData<Result<List<City>>>().apply {
        addSource(_searchResultLiveData) { this.value = it }
    }
    val findCityByNameUseCaseLD: LiveData<Result<List<City>>> = _findCityByNameUseCaseLD

    private fun switchProgress(result: Result<List<City>>) {
        _findCityByNameUseCaseLD.value = result
    }

    fun findCityByName(name: String) {
        _searchQuery.value = name
    }

    fun retry() {
        with(_searchQuery) {
            val currentValue = value
            value = ""
            value = currentValue
        }
    }

    /** Add city use case*/
    private val addCityUseCase = AddCityUseCase(repo)
    private val _addCityUseCaseLD = MutableLiveData<Result<Long>>()
    val addCityUseCaseLD: LiveData<Result<Long>> = _addCityUseCaseLD

    fun addCity(city: City) {
        launchUseCase(addCityUseCase, city) { _addCityUseCaseLD.value = it }
    }

    /** Add city by location use case*/
    private val addCityByLocUseCase = AddCityByLocUseCase(repo)
    private val _addCityByLocUseCaseLD = MutableLiveData<Result<Long>>()
    val addCityByLocUseCaseLD: LiveData<Result<Long>> = _addCityByLocUseCaseLD

    fun addCity(location: Location, lang: String) {
        val params = AddCityByLocUseCaseParams(location, lang)
        launchUseCase(addCityByLocUseCase, params) { _addCityByLocUseCaseLD.value = it }
    }

    /** Define Location use case*/
    private val defineLocUseCase = DefineLocUseCase(repo, locManager, locListener)
    private val _defineLocUseCaseLD = MutableLiveData<Result<Unit>>()
    val defineLocUseCaseLD: LiveData<Result<Unit>> = _defineLocUseCaseLD

    fun defineLoc() {
        launchUseCase(defineLocUseCase, Unit) { _defineLocUseCaseLD.value = it }
    }

    /** Others*/
    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(findCityByNameUseCase.javaClass.simpleName, findCityByNameUseCaseLD)
        put(defineLocUseCase.javaClass.simpleName, defineLocUseCaseLD)
        put(addCityUseCase.javaClass.simpleName, addCityUseCaseLD)
        put(addCityByLocUseCase.javaClass.simpleName, addCityByLocUseCaseLD)
    } as Map<String, LiveData<Result<*>>>
}