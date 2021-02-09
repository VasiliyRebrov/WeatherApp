package com.weather.viewmodel

import android.app.Application
import com.data.repo.GeneralRepo
import com.weather.viewmodel.BaseViewModel

class GeneralViewModel(application: Application, private val repo: GeneralRepo) : BaseViewModel<Unit>(
    application,
    repo
) {
}