package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.data.common.Result
import com.data.repo.CityItemRepo
import com.weather.viewmodel.BaseViewModel

class CityItemViewModel(application: Application, private val repo: CityItemRepo) : BaseViewModel<Unit>(
    application,
    repo
){

}