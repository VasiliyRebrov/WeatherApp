package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.data.common.Result


class CitiesManagerViewModel(application: Application, private val repo: CitiesManagerRepo) : AndroidViewModel(application)