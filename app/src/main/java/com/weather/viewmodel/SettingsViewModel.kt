package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.data.common.Result
import com.data.repo.SettingsRepo
import com.domain.usecases.TransformDataUseCase
import com.weather.common.entities.Config.Companion.getUnitMeasurePref

class SettingsViewModel(application: Application, repo: SettingsRepo) :
    BaseViewModel(application) {
    /** Transform data use case*/
    private val transformDataUseCase = TransformDataUseCase(repo)
    private val _transformDataUseCaseLD = MutableLiveData<Result<Int>>()
    val transformDataUseCaseLD: LiveData<Result<Int>> = _transformDataUseCaseLD

    fun transformData() {
        launchUseCase(transformDataUseCase, getApplication<Application>().getUnitMeasurePref()) {
            _transformDataUseCaseLD.value = it
        }
    }

    /** Others*/
    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(transformDataUseCase.javaClass.simpleName, transformDataUseCaseLD)
    } as Map<String, LiveData<Result<*>>>
}