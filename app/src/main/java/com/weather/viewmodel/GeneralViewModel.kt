package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.Result
import com.data.repo.BaseRepo
import com.domain.usecases.GetLocalCitiesUseCase
import kotlinx.coroutines.flow.map

class GeneralViewModel(application: Application, repo: BaseRepo) :
    BaseViewModel(application) {
    private val getLocalCitiesUseCases = GetLocalCitiesUseCase(repo)

    val localCitiesByPosLD = getLocalCitiesUseCases(Unit)
        .map {
            return@map if (it is Result.Success && it.data.isNotEmpty()) {
                Result.Success(it.data.sortedBy { city -> city.position })
            } else it
        }.asLiveData()

    override fun initLiveDataContainer() = mutableMapOf<String, LiveData<Result<*>>>().apply {
        put(getLocalCitiesUseCases.javaClass.simpleName, localCitiesByPosLD as LiveData<Result<*>>)
    }
}