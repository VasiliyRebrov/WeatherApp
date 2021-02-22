package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.data.common.Result
import com.data.repo.SettingsRepo
import com.domain.usecases.TransformDataUseCase
import com.weather.components.Config

class SettingsViewModel(application: Application, private val repo: SettingsRepo) :
    BaseViewModel(application, repo) {
    private val transformDataUseCase = TransformDataUseCase(repo)
    private val _transformDataUseCaseLD = MutableLiveData<Result<Unit>>()
    val transformDataUseCaseLD: LiveData<Result<Unit>> = _transformDataUseCaseLD

    fun transformData() {
        launchUseCase(transformDataUseCase, Config.getInstance(getApplication()).unitMeasurePref) {
            //обработка результата пересортировки
            _transformDataUseCaseLD.value = it
        }
    }

    override fun initLiveDataContainer() = mutableSetOf<LiveData<*>>().apply {
        add(transformDataUseCaseLD)
    } as Set<LiveData<Result<*>>>
}