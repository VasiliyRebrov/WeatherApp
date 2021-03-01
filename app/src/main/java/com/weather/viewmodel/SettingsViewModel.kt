package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.data.common.Result
import com.data.repo.SettingsRepo
import com.domain.usecases.TransformDataUseCase
import com.weather.common.entities.Config

class SettingsViewModel(application: Application, repo: SettingsRepo) :
    BaseViewModel(application) {

    private val transformDataUseCase = TransformDataUseCase(repo)
    private val _transformDataUseCaseLD = MutableLiveData<Result<Unit>>()
    val transformDataUseCaseLD: LiveData<Result<Unit>> = _transformDataUseCaseLD

    fun transformData() {
        launchUseCase(transformDataUseCase, Config.getInstance(getApplication()).unitMeasurePref) {
            _transformDataUseCaseLD.value = it
        }
    }

    override fun initLiveDataContainer() = mutableMapOf<String, LiveData<Result<*>>>().apply {
        put(transformDataUseCase.javaClass.simpleName, transformDataUseCaseLD as LiveData<Result<*>>)
    }
}