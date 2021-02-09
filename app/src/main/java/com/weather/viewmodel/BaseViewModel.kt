package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.data.repo.BaseRepo

abstract class BaseViewModel<T>(application: Application, private val repo: BaseRepo) :
    AndroidViewModel(application) {
}