package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.data.common.Result
import com.data.repo.SettingsRepo
import com.domain.usecases.TransformDataUC
import com.weather.common.entities.Config.Companion.getUnitMeasurePref

class SettingsViewModel(application: Application, repo: SettingsRepo) :
    BaseViewModel(application) {


    /** Transform data*/
    private val transformDataUC by lazy { TransformDataUC(repo) }
    private val _transformDataUCLD = MutableLiveData<Result<Int>>()
    val transformDataUCLD: LiveData<Result<Int>> = _transformDataUCLD

    fun transformData() {
        launchUseCase(transformDataUC, getApplication<Application>().getUnitMeasurePref()) {
            _transformDataUCLD.value = it
        }
    }

    override val useCases = mutableMapOf<String, LiveData<*>>().apply {
        put(transformDataUC.javaClass.simpleName, transformDataUCLD)
    } as Map<String, LiveData<Result<*>>>
}