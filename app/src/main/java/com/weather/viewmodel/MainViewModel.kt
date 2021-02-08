package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import com.data.model.City
import com.data.repo.MainRepository

class MainViewModel(application: Application, private val repo: MainRepository) :
    AndroidViewModel(application) {
    private val currentCities = mutableListOf<City>()
    val cities = repo.cities
    private val observer = Observer<List<City>> { newCities -> observeCities(newCities) }

    init {
        cities.observeForever(observer)
    }

    private fun observeCities(newCities: List<City>) {

    }
//
//    //улучшить метод, относительно новых условий
//    private fun observeCities(newList: List<City>) {
//        if (newList.size != currentCities.size) {
//            val toInsertItems = returnDifference(newList)
//            with(currentCities) { clear(); addAll(newList) }
//            refreshData(toInsertItems)
//        }
//    }
//
//    private fun refreshData(toInsertItems: List<City>) {
//        runCoroutine { repo.refreshData(toInsertItems) }
//    }
//
//    private fun returnDifference(it: List<City>) = mutableListOf<City>().apply {
//        addAll(it)
//        removeAll(currentCities)
//    }

    override fun onCleared() {
        cities.removeObserver(observer)
        super.onCleared()
    }
}