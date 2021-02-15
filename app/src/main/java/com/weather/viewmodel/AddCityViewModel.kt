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
import com.domain.usecases.addcity.AddCityByLocationUseCase
import com.domain.usecases.addcity.AddCityUseCase
import com.domain.usecases.addcity.DefineLocationUseCase
import com.domain.usecases.addcity.FindCityByNameUseCase
import kotlinx.coroutines.flow.*
import java.lang.Exception

class AddCityViewModel(application: Application, private val repo: AddCityRepo) :
    BaseViewModel(application, repo) {
    private val findCityUseCase by lazy { FindCityByNameUseCase(repo) }
    private val addCityUseCase by lazy { AddCityUseCase(repo) }
    private val addCityByLocationUseCase by lazy { AddCityByLocationUseCase(repo) }
    private val defineLocationUseCase by lazy {
        DefineLocationUseCase(repo, locManager, locListener)
    }

    private val _searchQuery = MutableStateFlow("")
    private val _searchResultLiveData = _searchQuery.asStateFlow()
        /**
         * первый map отвечает за отображение прогресса в зависимости от кол-ва символов
         * и затем просто пропускает аргумент дальше
         * какой способ корректнее?
         */
        .map {
            val result = try {
                if (it.length > 1) Result.Loading else throw InvalidArgsException()
            } catch (exc: Exception) {
                Result.Error(exc)
            }
            switchProgress(result)
            it
//            val result = if (it.length > 1) Result.Loading else Result.Error(InvalidArgsException())
//            switchProgress(result)
//            it
        }
        .debounce(1200)
        .filter { it.length > 1 }
        .mapLatest { name ->
            return@mapLatest findCityUseCase(name)
        }
        .asLiveData()

    private val _findCityUseCaseLiveData = MediatorLiveData<Result<List<City>>>().apply {
        addSource(_searchResultLiveData) { this.value = it }
    }
    val findCityUseCaseLiveData: LiveData<Result<List<City>>> = _findCityUseCaseLiveData

    private val _addCityUseCaseLiveData = MutableLiveData<Result<Int>>()
    val addCityUseCaseLiveData: LiveData<Result<Int>> = _addCityUseCaseLiveData

    private val _addCityByLocationUseCaseLiveData = MutableLiveData<Result<Int>>()
    val addCityByLocationUseCaseLiveData: LiveData<Result<Int>> = _addCityByLocationUseCaseLiveData

    private val _defineLocationUseCaseLiveData = MutableLiveData<Result<Unit>>()
    val defineLocationUseCaseLiveData: LiveData<Result<Unit>> = _defineLocationUseCaseLiveData

    private fun switchProgress(result: Result<List<City>>) {
        _findCityUseCaseLiveData.value = result
    }

    /** проблема: при onResume() считывание EditText происходит автоматически. вызывается этот метод
     * решение: источник, принимающий параметр - stateFlow. В случае передачи тех же данных, которые
     * в нем уже находятся, он их не примет. Следовательно, никаких повторных запросов не происходит*/
    fun search(name: String) {
        _searchQuery.value = name
    }


    fun addCity(city: City) {
        launchUseCase(addCityUseCase, city) {
            // обработка при добавлении города
            _addCityUseCaseLiveData.value = it
        }
    }

    fun addCity(location: Location) {
        launchUseCase(addCityByLocationUseCase, location) {
            // обработка при добавлении города по локации
            _addCityByLocationUseCaseLiveData.value = it
        }
    }

    //вызывается при нажатии кнопки
    fun defineLocation() {
        launchUseCase(defineLocationUseCase, Unit) {
            // обработка определения локации
            // при получении локации, юзкейс доб. города вызовется автоматически. см. Listener
            _defineLocationUseCaseLiveData.value = it
        }
    }


    private val locManager by lazy { application.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val locListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let { locManager.removeUpdates(this); addCity(it) }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun initLiveDataContainer() = mutableSetOf<LiveData<*>>().apply {
        add(_addCityUseCaseLiveData)
        add(_findCityUseCaseLiveData)
        add(_addCityByLocationUseCaseLiveData)
        add(_defineLocationUseCaseLiveData)
    } as Set<LiveData<Result<*>>>
}