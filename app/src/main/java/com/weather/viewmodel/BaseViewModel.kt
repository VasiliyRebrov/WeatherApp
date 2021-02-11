package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.Result
import com.data.repo.BaseRepo

abstract class BaseViewModel<T>(application: Application, private val repo: BaseRepo) :
    AndroidViewModel(application) {


    protected fun MediatorLiveData<Boolean>.switchProgress(source: Result<*>?) {
        value = source is Result.Loading
    }

    protected fun MediatorLiveData<Result.Error>.setError(source: Result<*>) {
        if (source is Result.Error) value = source
    }
}

