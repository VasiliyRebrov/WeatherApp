package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.data.common.Result
import com.data.repo.SettingsRepo
import com.weather.viewmodel.BaseViewModel

class SettingsViewModel(application: Application, private val repo: SettingsRepo) :
 AndroidViewModel(application)