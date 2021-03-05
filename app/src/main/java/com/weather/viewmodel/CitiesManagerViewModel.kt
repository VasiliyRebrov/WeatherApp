package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.data.common.Result
import com.data.model.City
import com.domain.usecases.DeleteCityUC
import com.domain.usecases.GetCityDataListUC
//import com.domain.usecases.GetCityCurrentWeatherRelationListUseCase
import com.domain.usecases.ReorderCitiesUC
import kotlinx.coroutines.flow.map


class CitiesManagerViewModel(application: Application, private val repo: CitiesManagerRepo) :
    BaseViewModel(application) {


    /** Get city with data list*/
    val getCityDataListUCLD =GetCityDataListUC(repo)
    val cityCurrentWeatherRelationListLiveData =
        getCityDataListUCLD(Unit).map { result ->
            return@map if (result is Result.Success) {
                Result.Success(result.data.sortedBy { it.city.position })
            } else result
        }.asLiveData()

    /** Delete city*/
    private val deleteCityUC =DeleteCityUC(repo)
    private val _deleteCityUCLD = MutableLiveData<Result<Int>>()
    val deleteCityUCLD: LiveData<Result<Int>> = _deleteCityUCLD

    fun deleteCity(city: City) {
        launchUseCase(deleteCityUC, city) {
            _deleteCityUCLD.value = it
        }
    }

    /** Reorder cities */
    private val reorderCitiesUC = ReorderCitiesUC(repo)
    private val _reorderCitiesUCLD = MutableLiveData<Result<Int>>()
    val reorderCitiesUCLD: LiveData<Result<Int>> = _reorderCitiesUCLD


    fun reorderCities(reorderedCities: List<City>) {
        launchUseCase(reorderCitiesUC, reorderedCities) {
            _reorderCitiesUCLD.value = it
        }
    }


    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(
            getCityDataListUCLD.javaClass.simpleName,
            cityCurrentWeatherRelationListLiveData
        )
        put(deleteCityUC.javaClass.simpleName, deleteCityUCLD)
        put(
            reorderCitiesUC.javaClass.simpleName,
            reorderCitiesUCLD
        )
    } as Map<String, LiveData<Result<*>>>
}