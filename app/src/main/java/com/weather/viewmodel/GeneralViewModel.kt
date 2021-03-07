package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.Result
import com.data.repo.BaseRepo
import com.domain.usecases.GetCitiesUseCase
import kotlinx.coroutines.flow.map

class GeneralViewModel(application: Application, repo: BaseRepo) :
    BaseViewModel(application) {
    /** Get local cities use case*/
    private val getCitiesUseCase = GetCitiesUseCase(repo)
    val localCitiesByPosLD = getCitiesUseCase(Unit)
        .map {
            return@map if (it is Result.Success && it.data.isNotEmpty())
                Result.Success(it.data.sortedBy { city -> city.position })
            else
                it
        }.asLiveData()

    /** Others*/
    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(getCitiesUseCase.javaClass.simpleName, localCitiesByPosLD)
    } as Map<String, LiveData<Result<*>>>
}