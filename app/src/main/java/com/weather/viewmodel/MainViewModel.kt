package com.weather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.data.common.Result
import com.data.common.data
import com.data.common.succeeded
import com.data.model.City
import com.data.repo.MainRepo
import com.domain.usecases.main.GetLocalCitiesUseCase
import com.domain.usecases.main.RefreshWeatherDataUseCase
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class MainViewModel(application: Application, private val repo: MainRepo) :
    BaseViewModel(application, repo) {

    suspend fun isExistCities() = repo.isExistCities()


    val getLocalCitiesUseCase = GetLocalCitiesUseCase(repo)
    val refreshWeatherDataUseCase = RefreshWeatherDataUseCase(repo)

    private val _refreshWeatherDataUseCaseLD = MutableLiveData<Result<String>>()
    val refreshWeatherDataUseCaseLD: LiveData<Result<String>> = _refreshWeatherDataUseCaseLD

    private val currentCities = mutableListOf<City>()

    /**
     * в начале вернет Loading благодаря stateFlow
     * stateFlow здесь фильтрует на уникальное значение. Единственный полезный для этого случай
     * - пересортировка.
     * Когда она происходит - меняется поле pos. Но его не охватывают проверки equals, а также
     * здесь список сортируется по id(в usecase). Это значит, про при пересортировке мы получаем
     * одинаковый список. stateFlow его не примет, и следовательно, наблюдатели не сработают
     *
     */
    val localCitiesLiveData =
        getLocalCitiesUseCase(Unit).stateIn(viewModelScope, Lazily, Result.Loading).asLiveData()

    private val observer =
        Observer<Result<List<City>>> { localCities -> observeCities(localCities) }

    init {
        localCitiesLiveData.observeForever(observer)
    }

    /**
     * метод будет вызван только когда содержание списка изменилось (добавление/удаление)
     * не будет вызван, когда была пересортировка элементов
     * также будет вызван с Loading при инициализации
     * */
    private fun observeCities(localCities: Result<List<City>>) {
        Log.d("myTag", "observeCities : $localCities")
        if (localCities is Result.Success) {
            val pair = defineDifference(localCities.data)
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