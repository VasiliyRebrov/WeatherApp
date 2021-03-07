package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.data.common.Result
import com.data.model.City
import com.data.repo.BaseRepo
import com.domain.usecases.GetCitiesUseCase
import com.domain.usecases.RefreshDataUseCase
import com.domain.usecases.RefreshWeatherUseCaseParams
import com.weather.common.entities.Config.Companion.getUnitMeasurePref
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*

class MainViewModel(application: Application, repo: BaseRepo) : BaseViewModel(application) {
    private val actualCities = mutableListOf<City>()
    var focusedCityPos = 0

    /** Get local cities use case*/
    private val getCitiesUseCase = GetCitiesUseCase(repo)

    /**
     * [localCitiesByIdLD] Не может быть [null]. По умолчанию [Result.Loading].
     * Полученный список сортируется по [City.cityId].
     * [StateFlow] пропускает только уникальное значение, что гарантирует получение списка на удаление\добавление только при его фактическом изменении.
     * События, фактически не меняющие список, такие как пересорировка - они не придут сюда, тк при этом меняется поле [City.position],которое не охватывается [equals]
     * */
    val localCitiesByIdLD = getCitiesUseCase(Unit)
        .map {
            return@map if (it is Result.Success && it.data.isNotEmpty())
                Result.Success(it.data.sortedBy { city -> city.cityId })
            else it
        }.stateIn(viewModelScope, Eagerly, Result.Loading).asLiveData()

    private val observer =
        Observer<Result<List<City>>> { localCities -> observeCities(localCities) }

    init {
        localCitiesByIdLD.observeForever(observer)
    }

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
        val params = RefreshWeatherUseCaseParams(
            getApplication<Application>().getUnitMeasurePref(),
            currentLang,
            newCities,
            oldCities,
        )
        launchUseCase(refreshDataUseCase, params) {
            _refreshDataUseCaseLD.value = it
        }
    }

    /** Refresh data use case*/
    private val refreshDataUseCase = RefreshDataUseCase(repo)
    private val _refreshDataUseCaseLD = MutableLiveData<Result<String>>()
    val refreshDataUseCaseLD: LiveData<Result<String>> = _refreshDataUseCaseLD

    /** Others*/
    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(refreshDataUseCase.javaClass.simpleName, refreshDataUseCaseLD)
        put(getCitiesUseCase.javaClass.simpleName, localCitiesByIdLD)
    } as Map<String, LiveData<Result<*>>>

    override fun onCleared() {
        localCitiesByIdLD.removeObserver(observer)
        super.onCleared()
    }
}