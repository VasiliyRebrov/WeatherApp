package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.data.common.Result
import com.data.common.data
import com.data.model.City
import com.data.model.CityCurrentWeatherRelation
import com.domain.usecases.citiesmanager.DeleteCityUseCase
import com.domain.usecases.citiesmanager.GetCityCurrentWeatherRelationListUseCase
import com.domain.usecases.citiesmanager.ReorderLocalCitiesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn


class CitiesManagerViewModel(application: Application, private val repo: CitiesManagerRepo) :
    BaseViewModel(application, repo) {

    val getCityCurrentWeatherRelationListUseCase = GetCityCurrentWeatherRelationListUseCase(repo)

    // вне зависимости от нижней реализации, первым livdata шлет null
    //возможное решение - преобразовывать с помощью SHAREDflow с 1-эл. кэшем(не state, потому что нам не надо
    // отбрасывать, если результат одинаковый.)
    val cityCurrentWeatherRelationListLiveData =
        getCityCurrentWeatherRelationListUseCase(Unit).asLiveData()

//    val localCitiesLiveData =
//        getLocalCitiesUseCase(Unit).map {
//            val text = when (it) {
//                is Result.Success -> "Success"
//                is Result.Error -> "Error"
//                is Result.Loading -> "Loading"
//            }
//            return@map it.data?: listOf()
//        }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())
//            .asLiveData()


    private val deleteCityUseCase = DeleteCityUseCase(repo)
    private val _deleteCityUseCaseLD = MutableLiveData<Result<Int>>()
    val deleteCityUseCaseLD: LiveData<Result<Int>> = _deleteCityUseCaseLD

    private val reorderLocalCitiesUseCase = ReorderLocalCitiesUseCase(repo)
    private val _reorderLocalCitiesUseCaseLD = MutableLiveData<Result<Int>>()
    val reorderLocalCitiesUseCaseLD: LiveData<Result<Int>> = _reorderLocalCitiesUseCaseLD


    fun deleteCity(city: City) {
        launchUseCase(deleteCityUseCase, city) {
            //обработка удаления города
            _deleteCityUseCaseLD.value = it
        }
    }

    fun reorderLocalCities(reorderedCities: List<City>) {
        launchUseCase(reorderLocalCitiesUseCase, reorderedCities) {
            //обработка пересортировка городов
            _reorderLocalCitiesUseCaseLD.value = it
        }
    }

    override fun initLiveDataContainer() = mutableSetOf<LiveData<*>>().apply {
        add(_deleteCityUseCaseLD)
        add(_reorderLocalCitiesUseCaseLD)
    } as Set<LiveData<Result<*>>>
}