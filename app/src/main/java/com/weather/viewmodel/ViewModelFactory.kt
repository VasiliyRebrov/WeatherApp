package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.data.model.City
import com.data.repo.*

class ViewModelFactory(
    private val viewClassName: String,
    private val application: Application,
    private val city: City? = null
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (viewClassName) {
        "MainActivity" -> MainViewModel(application, BaseRepo(application))
        "GeneralFragment" -> GeneralViewModel(application, BaseRepo(application))
        "CityItemFragment" -> CityItemViewModel(application, city!!, CityItemRepo(application))
        "AddCityFragment" -> AddCityViewModel(application, AddCityRepo(application))
        "SettingsFragment" -> SettingsViewModel(application, SettingsRepo(application))
        "CitiesManagerFragment" -> CitiesManagerViewModel(
            application,
            CitiesManagerRepo(application)
        )
        else -> throw Exception()
    } as T
}
