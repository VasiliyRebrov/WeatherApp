package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application


class CitiesManagerViewModel(application: Application, private val repo: CitiesManagerRepo) :
    BaseViewModel<Unit>(application, repo) {
}