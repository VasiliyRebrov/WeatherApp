package com.weather.viewmodel

import android.app.Application
import com.data.repo.SettingsRepo
import com.weather.viewmodel.BaseViewModel

class SettingsViewModel(application: Application, private val repo: SettingsRepo) :
    BaseViewModel<Unit>(
        application,
        repo
    )