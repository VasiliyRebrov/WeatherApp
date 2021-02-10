package com.weather.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.lifecycle.*
import com.data.common.AddCityUseCases
import com.data.common.Result
import com.data.model.City
import com.data.repo.AddCityRepo
import com.domain.AddCityByLocationUseCase
import com.domain.AddCityUseCase
import com.domain.DefineLocationUseCase
import com.domain.FindCityByNameUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class AddCityViewModel(application: Application, private val repo: AddCityRepo) :
    BaseViewModel<AddCityUseCases>(application, repo) {
    val findCityUseCase by lazy { FindCityByNameUseCase(repo) }
    val addCityUseCase by lazy { AddCityUseCase(repo) }
    val addCityByLocationUseCase by lazy { AddCityByLocationUseCase(repo) }
    val defineLocationUseCase by lazy { DefineLocationUseCase(repo, locManager, locListener) }

    private val _searchQuery = MutableStateFlow("")
    private val _searchResultLiveData = _searchQuery.asStateFlow()
        /**
         * первый map отвечает за отображение прогресса в зависимости от кол-ва символов
         * и затем просто пропускает значение дальше
         */
        .map {
            /**
             * Error должен хранить ошибку "нет текста или типо того"
             */
            val result = if (it.length > 1) Result.Loading else Result.Error(Exception())
            switchProgress(result)
            it
        }
        .debounce(1200)
        .filter { it.length > 1 }
        .mapLatest { name ->
            return@mapLatest findCityUseCase(name)
        }
        /**
         * ошибки здесь не обрабатываются, потому что все обрабатывается в usecase, где они
         * просто оборачиваются в Result
         * если использовать catch здесь - прервется flow.
         */
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

    fun search(name: String) {
        _searchQuery.value = name
    }


    fun addCity(city: City) {
        viewModelScope.launch {
            addCityUseCase(city).collect {
                // обработка при добавлении города
                _addCityUseCaseLiveData.value = it
            }
        }
    }

    fun addCity(location: Location) {
        viewModelScope.launch {
            addCityByLocationUseCase(location).collect {
                // обработка при добавлении города по локации
                _addCityByLocationUseCaseLiveData.value = it
            }
        }
    }

    //вызывается при нажатии кнопки
    fun defineLocation() {
        viewModelScope.launch {
            defineLocationUseCase(Unit).collect {
                // обработка определения локации
                // при получении локации, юзкейс доб. города вызовется автоматически. см. Listener
                _defineLocationUseCaseLiveData.value = it
            }
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
}

//каждый метод, где запускается юзкейс - он шаблонный. сделать один и передавать ему нужные данные?