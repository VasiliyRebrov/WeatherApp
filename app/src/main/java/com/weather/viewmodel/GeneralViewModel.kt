package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.data.common.Result
import com.data.repo.GeneralRepo
import com.weather.viewmodel.BaseViewModel

class GeneralViewModel(application: Application, private val repo: GeneralRepo) :  AndroidViewModel(application)