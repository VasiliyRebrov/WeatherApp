package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.MediatorSingleLiveEvent
import com.data.common.Result
import com.data.repo.BaseRepo

abstract class BaseViewModel<T>(application: Application, private val repo: BaseRepo) :
    AndroidViewModel(application) {

    private val _progressLiveData = MediatorLiveData<Boolean>()
    val progressLiveData: LiveData<Boolean> = _progressLiveData

    protected fun initProgress(action: MediatorLiveData<Boolean>.() -> Unit) {
        _progressLiveData.action()
    }

//    private val _errorEvent = MediatorSingleLiveEvent<Result.Error>()
//    private val errorEvent: LiveData<Result.Error> = _errorEvent


    protected fun MediatorLiveData<Boolean>.switchProgress(source: Result<*>?) {
        value = source is Result.Loading
    }

    protected fun MediatorLiveData<Result.Error>.setError(source: Result<*>) {
        if (source is Result.Error) value = source
    }

}

