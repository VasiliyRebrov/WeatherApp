package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.data.common.Result
import com.data.repo.SettingsRepo
import com.domain.usecases.settings.ReConfigDataUseCase
import com.weather.viewmodel.BaseViewModel

class SettingsViewModel(application: Application, private val repo: SettingsRepo) :
    BaseViewModel(application, repo) {
    private val reConfigDataUseCase = ReConfigDataUseCase(repo)
    private val _reconfigDataLD = MutableLiveData<Result<Unit>>()
    val reconfigDataLD: LiveData<Result<Unit>> = _reconfigDataLD

    fun reConfig() {
        launchUseCase(reConfigDataUseCase, Unit) {
            //обработка результата пересортировки
            _reconfigDataLD.value = it
        }
    }

    override fun initLiveDataContainer() = mutableSetOf<LiveData<*>>().apply {
        add(reconfigDataLD)
    } as Set<LiveData<Result<*>>>
}