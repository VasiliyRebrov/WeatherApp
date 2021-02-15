package com.weather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.data.common.Result
import com.data.common.data
import com.data.model.City
import com.data.repo.MainRepo
import com.domain.usecases.main.GetLocalCitiesUseCase
import com.domain.usecases.main.RefreshWeatherDataUseCase
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(application: Application, private val repo: MainRepo) :
    BaseViewModel(application, repo) {

    val getLocalCitiesUseCase = GetLocalCitiesUseCase(repo)
    val refreshWeatherDataUseCase = RefreshWeatherDataUseCase(repo)

    private val _refreshWeatherDataUseCaseLD = MutableLiveData<Result<String>>()
    val refreshWeatherDataUseCaseLD: LiveData<Result<String>> = _refreshWeatherDataUseCaseLD

    private val currentCities = mutableListOf<City>()

    /**
     * перехватить где-нибудь, если идет пустой список (при инициализации он идет)
     * сейчас перехват в методе
     */
    val localCitiesLiveData =
        getLocalCitiesUseCase(Unit).map { it.data!! }.stateIn(viewModelScope, Lazily, listOf())
            .asLiveData()

    private val observer = Observer<List<City>> { localCities -> observeCities(localCities) }

    init {
        localCitiesLiveData.observeForever(observer)
    }

    /**
     * метод будет вызван только когда содержание списка изменилось (добавление/удаление)
     * не будет вызван, когда была пересортировка элементов
     * также будет вызван с пустым списком (при инициализации)
     * */
    private fun observeCities(localCities: List<City>) {
        Log.d("myTag", "observeCities : $localCities")
        if (localCities.isNotEmpty()) {
            val pair = defineDifference(localCities)
            refreshData(pair)
        }
    }

    private fun defineDifference(localCities: List<City>): Pair<List<City>, List<City>> {
        fun createCollection(toAdd: List<City>, toRemove: List<City>) =
            mutableListOf<City>().apply { addAll(toAdd);removeAll(toRemove) }

        val newCities = createCollection(localCities, currentCities)
        val oldCities = createCollection(currentCities, localCities)
        with(currentCities) { clear();addAll(localCities) }
        return Pair(newCities, oldCities)
    }

    private fun refreshData(pair: Pair<List<City>, List<City>>) {
        launchUseCase(refreshWeatherDataUseCase, pair) {
            //обработка обновление погодных данных
            _refreshWeatherDataUseCaseLD.value = it
        }
    }

    override fun initLiveDataContainer() = mutableSetOf<LiveData<*>>().apply {
        add(_refreshWeatherDataUseCaseLD)
    } as Set<LiveData<Result<*>>>


    override fun onCleared() {
        localCitiesLiveData.removeObserver(observer)
        super.onCleared()
    }
}