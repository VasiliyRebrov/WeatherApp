package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.Result
import com.data.repo.BaseRepo
import com.domain.usecases.GetCitiesUC
import kotlinx.coroutines.flow.map

class GeneralViewModel(application: Application, repo: BaseRepo) :
    BaseViewModel(application) {


    /** Get local cities*/
    private val getLocalCitiesUseCases=GetCitiesUC(repo)
    val localCitiesByPosLD = getLocalCitiesUseCases(Unit)
        .map {
            return@map if (it is Result.Success && it.data.isNotEmpty()) {
                Result.Success(it.data.sortedBy { city -> city.position })
            } else
                it
        }.asLiveData()

    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(getLocalCitiesUseCases.javaClass.simpleName, localCitiesByPosLD)
    } as Map<String, LiveData<Result<*>>>
}