package com.weather.viewmodel

import com.data.repo.CitiesManagerRepo
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.data.model.City
import com.data.repo.*
import java.lang.Exception

class ViewModelFactory(
    private val name: String,
    private val application: Application,
    private val city: City? = null
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (name) {
        "CityItemViewModel" -> CityItemViewModel(
            application,
            city!!,
            CityItemRepo(application)
        )
        "GeneralViewModel" -> GeneralViewModel(
            application,
            BaseRepo(application)
        )
        "CitiesManagerViewModel" -> CitiesManagerViewModel(
            application,
            CitiesManagerRepo(application)
        )
        "AddCityViewModel" -> AddCityViewModel(
            application,
            AddCityRepo(application)
        )
        "SettingsViewModel" -> SettingsViewModel(
            application,
            SettingsRepo(application)
        )
        "MainViewModel" -> MainViewModel(
            application,
            BaseRepo(application)
        )
        else -> throw Exception()
    } as T
}
