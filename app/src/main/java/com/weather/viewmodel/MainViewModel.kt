package com.weather.viewmodel

import android.app.Application
import com.data.repo.MainRepo
import com.weather.viewmodel.BaseViewModel

class MainViewModel(application: Application, private val repo: MainRepo) :
    BaseViewModel<Unit>(application, repo)