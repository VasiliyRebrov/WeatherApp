package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.Result
import com.data.model.City
import com.data.repo.GeneralRepo
import com.domain.usecases.main.GetLocalCitiesUseCase
import com.weather.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.map

class GeneralViewModel(application: Application, private val repo: GeneralRepo) :
    BaseViewModel(application, repo) {
    private val getLocalCitiesUseCases = GetLocalCitiesUseCase(repo)

    val localCitiesByPosLD = getLocalCitiesUseCases(Unit)
        .map {
            return@map if (it is Result.Success && it.data.isNotEmpty()) {
                Result.Success(it.data.sortedBy { city -> city.position })
            } else it
        }.asLiveData()

    override fun initLiveDataContainer(): Set<LiveData<Result<*>>> {
        TODO("Not yet implemented")
    }
}