package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.data.common.AddCityUseCases
import com.data.common.Result
import com.data.model.City
import com.data.repo.AddCityRepo
import com.domain.FindCityByNameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception

class AddCityViewModel(application: Application, private val repo: AddCityRepo) :
    BaseViewModel<AddCityUseCases>(application, repo) {
    val findCityUseCase = FindCityByNameUseCase(repo, Dispatchers.IO)

    private val _searchQuery = MutableStateFlow("")
    private val _searchResultLiveData = _searchQuery.asStateFlow()
        /**
         * первый map отвечает за отображение прогресса в зависимости от кол-ва символов
         * и затем просто пропускает значение дальше
         */
        .map {
            /**
             * Error должен хранить ошибку "нет текста или типо того"
             */
            val result = if (it.length > 1) Result.Loading else Result.Error(Exception())
            switchProgress(result)
            it
        }
        .debounce(1200)
        .filter { it.length > 1 }
        .mapLatest { name ->
            return@mapLatest findCityUseCase(name)
        }
        /**
         * ошибки здесь не обрабатываются, потому что все обрабатывается в usecase, где они
         * просто оборачиваются в Result
         * если использовать catch здесь - прервется flow.
         */
        .asLiveData()

    fun switchProgress(res: Result<Nothing>) {
        _myLD.value = res
    }

    private val _myLD = MediatorLiveData<Result<List<City>>>().apply {
        addSource(_searchResultLiveData) {
            this.value = it
        }
    }
    val myLD: LiveData<Result<List<City>>> = _myLD


    fun search(name: String) {
        _searchQuery.value = name
    }
}