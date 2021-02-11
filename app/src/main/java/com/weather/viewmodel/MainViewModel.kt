package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.data.common.Result
import com.data.repo.MainRepo
import com.weather.viewmodel.BaseViewModel

class MainViewModel(application: Application, private val repo: MainRepo) :
    AndroidViewModel(application)