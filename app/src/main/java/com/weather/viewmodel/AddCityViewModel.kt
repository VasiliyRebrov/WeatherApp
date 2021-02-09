package com.weather.viewmodel

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.data.common.AddCityUseCases
import com.data.common.NetworkProviderDisabledException
import com.data.common.Result
import com.data.model.City
import com.data.repo.AddCityRepo
import com.domain.AddCityUseCase
import com.domain.AddNearCityByLocationUseCase
import com.domain.FindCityByNameUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class AddCityViewModel(application: Application, private val repo: AddCityRepo) :
    BaseViewModel<AddCityUseCases>(application, repo) {
    val findCityUseCase = FindCityByNameUseCase(repo)
    val addCityUseCase = AddCityUseCase(repo)
    val addNearCityByLocationUseCase = AddNearCityByLocationUseCase(repo)

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

    private fun switchProgress(res: Result<Nothing>) {
        _myLD.value = res
    }

    private val _myLD = MediatorLiveData<Result<List<City>>>().apply {
        addSource(_searchResultLiveData) {
            this.value = it
        }
    }
    val myLD: LiveData<Result<List<City>>> = _myLD

    val addCItyLD = MutableLiveData<Result<Int>>()
    fun search(name: String) {
        _searchQuery.value = name
    }

    fun addCity(city: City) {
        viewModelScope.launch {
            flow<String> {  }.asL
            addCItyLD
            val kek = addCityUseCase(city)
        }
    }

    fun addNearCity(location: Location) {
        viewModelScope.launch {
            val kek = addNearCityByLocationUseCase(location)
        }
    }

    fun addCityByLocation() {
        defineLocation()
    }


    fun defineLocation(): AddCityUseCases {
        if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(
                    getApplication(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //переключаемся на главный, чтобы работал слушатель
//                withContext(Dispatchers.Main) {
                locManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    locListener
                )
//                }
            }
        } else throw NetworkProviderDisabledException()
        return AddCityUseCases.SETUPLOCATION
    }

    private val locManager by lazy { application.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val locListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let { locManager.removeUpdates(this); addNearCity(it) }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }
}