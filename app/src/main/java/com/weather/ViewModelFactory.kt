package com.weather

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.data.model.City
import com.data.repo.AddCityRepo
import java.lang.Exception

class ViewModelFactory(
    private val name: String,
    private val application: Application,
    private val city: City? = null
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (name) {
        "AddCityViewModel" -> AddCityViewModel(
            application, AddCityRepo()
        )
        else -> throw Exception()
    } as T
}