package com.weather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import com.domain.RefreshWeatherParams
import com.domain.usecases.GetLocalCitiesUseCase
import com.domain.usecases.RefreshWeatherDataUseCase
import com.weather.components.Config
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*

class MainViewModel(application: Application, repo: BaseRepo) :
    BaseViewModel(application) {
    private val currentCities = mutableListOf<City>()

    private val refreshWeatherDataUseCase = RefreshWeatherDataUseCase(repo)
    private val _refreshWeatherDataUseCaseLD = MutableLiveData<Result<String>>()
    val refreshWeatherDataUseCaseLD: LiveData<Result<String>> = _refreshWeatherDataUseCaseLD


    /**
     * в начале вернет по умолчанию Loading благодаря stateFlow
     * stateFlow здесь фильтрует на уникальное значение. Единственный полезный для этого случай
     * - пересортировка.
     * Когда она происходит - меняется поле pos. Но его не охватывают проверки equals, а также
     * здесь список сортируется по id(в usecase). Это значит, про при пересортировке мы получаем
     * одинаковый список. stateFlow его не примет, и следовательно, наблюдатели не сработают
     *
     */

    /** полученный список городов сортируется по id.*/
    /** сортировка по id позволяет исключить изменения, после юзкейса пересортировки,
     * когда меняется поле pos */
    private val getLocalCitiesUseCase = GetLocalCitiesUseCase(repo)

    /**
     * [localCitiesLiveData] по умолчанию [Result.Loading]
     * не может быть [null]
     *
     * */
    val localCitiesLiveData = getLocalCitiesUseCase(Unit)
        .map {
            return@map if (it is Result.Success && it.data.isNotEmpty()) {
                Result.Success(it.data.sortedBy { city -> city.cityId })
            } else it
        }.stateIn(viewModelScope, Eagerly, Result.Loading).asLiveData()

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
            val (newCities, oldCities) = defineDifference(localCities.data)
            refreshData(newCities, oldCities)
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

    private fun refreshData(newCities: List<City>, oldCities: List<City>) {
        val params = RefreshWeatherParams(
            Config.getInstance(getApplication()).unitMeasurePref,
            newCities,
            oldCities
        )
        launchUseCase(refreshWeatherDataUseCase, params) {
            _refreshWeatherDataUseCaseLD.value = it
        }
    }

    override fun initLiveDataContainer() = mutableMapOf<String, LiveData<Result<*>>>().apply {
        put(
            refreshWeatherDataUseCase.javaClass.simpleName,
            refreshWeatherDataUseCaseLD as LiveData<Result<*>>
        )
        put(getLocalCitiesUseCase.javaClass.simpleName, localCitiesLiveData as LiveData<Result<*>>)
    }

    override fun onCleared() {
        localCitiesLiveData.removeObserver(observer)
        super.onCleared()
    }
}