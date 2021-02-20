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
            GeneralRepo(application)
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
        "MainViewModel" -> MainViewModel(application, MainRepo(application))
        else -> throw Exception()
    } as T

//    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (name) {
//        ::GeneralViewModel ->
//            CityItemViewModel(application, CityItemRepository(application, city!!)) as T
//        GeneralViewModel::class ->
//            GeneralViewModel(application, GeneralRepository(application)) as T
//        CitiesManagerViewModel::class ->
//            CitiesManagerViewModel(application, CitiesManagerRepository(application)) as T
//    }
}


//class CitiesManagerViewModelFactory(private val application: Application) :
//    ViewModelProvider.NewInstanceFactory() {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
//        CitiesManagerViewModel(application, CitiesManagerRepository(application)) as T
//}


//class FeedViewModelFactory(
//    private val application: Application,
//    private val categoryName: String
//) : ViewModelProvider.NewInstanceFactory() {
//    private val repository = Repository.getRepository(application)
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>) =
//        FeedViewModel(application, repository, categoryName) as T
//}
//
