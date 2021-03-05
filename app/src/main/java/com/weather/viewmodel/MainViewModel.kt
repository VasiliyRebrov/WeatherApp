package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import com.domain.usecases.GetCitiesUC
import com.domain.usecases.RefreshDataUC
import com.domain.usecases.RefreshWeatherParams
import com.weather.common.entities.Config.Companion.getUnitMeasurePref
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*

class MainViewModel(application: Application, repo: BaseRepo) : BaseViewModel(application) {
    private val actualCities = mutableListOf<City>()
    var focusedCityPos = 0

    override fun onCleared() {
        localCitiesLD.removeObserver(observer)
        super.onCleared()
    }
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

    /** Get local cities*/
    private val getCitiesUC by lazy { GetCitiesUC(repo) }

    /**
     * [localCitiesLD] по умолчанию [Result.Loading]. Не может быть [null]
     * */
    val localCitiesLD = getCitiesUC(Unit)
        .map {
            return@map if (it is Result.Success && it.data.isNotEmpty()) {
                Result.Success(it.data.sortedBy { city -> city.cityId })
            } else it
        }.stateIn(viewModelScope, Eagerly, Result.Loading).asLiveData()

    private val observer =
        Observer<Result<List<City>>> { localCities -> observeCities(localCities) }

    init {
        localCitiesLD.observeForever(observer)
    }

    /**
     * метод будет вызван только когда содержание списка изменилось (добавление/удаление)
     * не будет вызван, когда была пересортировка элементов
     * также будет вызван с Loading при инициализации
     * */
    private fun observeCities(localCities: Result<List<City>>) {
        if (localCities is Result.Success) {
            val (newCities, oldCities) = defineDifference(localCities.data)
            refreshData(newCities, oldCities)
        }
    }

    private fun defineDifference(localCities: List<City>): Pair<List<City>, List<City>> {
        fun createCollection(toAddCities: List<City>, toRemoveCities: List<City>) =
            mutableListOf<City>().apply { addAll(toAddCities);removeAll(toRemoveCities) }.toList()

        val newCities = createCollection(localCities, actualCities)
        val oldCities = createCollection(actualCities, localCities)
        with(actualCities) { clear();addAll(localCities) }
        return Pair(newCities, oldCities)
    }

    private fun refreshData(newCities: List<City>, oldCities: List<City>) {
        val params = RefreshWeatherParams(
            getApplication<Application>().getUnitMeasurePref(),
            currentLang,
            newCities,
            oldCities,
        )
        launchUseCase(refreshDataUC, params) {
            _refreshDataUCLD.value = it
        }
    }

    /** Refresh data*/
    private val refreshDataUC by lazy { RefreshDataUC(repo) }
    private val _refreshDataUCLD = MutableLiveData<Result<String>>()
    val refreshDataUCLD: LiveData<Result<String>> = _refreshDataUCLD

    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(refreshDataUC.javaClass.simpleName, refreshDataUCLD)
        put(getCitiesUC.javaClass.simpleName, localCitiesLD)
    } as Map<String, LiveData<Result<*>>>
}