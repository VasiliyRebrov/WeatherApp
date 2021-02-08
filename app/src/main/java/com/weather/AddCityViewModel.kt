package com.weather

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.data.common.NoNetworkException
import com.data.common.Result
import com.data.model.City
import com.data.repo.AddCityRepo
import com.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class AddCityViewModel(application: Application, private val repo: AddCityRepo) :
    AndroidViewModel(application) {
    val findCityUseCase = FindCityByNameUseCase(repo, Dispatchers.IO)

    private val _searchQuery = MutableStateFlow("")

    //добавить прогресс - единственное видимое решение, это сделать отдельно ливДату с типом Result
    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResultLiveData = _searchQuery.asStateFlow()
        .debounce(1200)
        .filter { it.length > 1 }
        .mapLatest { query -> kek(query) }
        .retryWhen { cause, attempt ->
            Log.d("Tag", "retry after $cause | $attempt ")
            if (cause is NoNetworkException) {
                delay(3000)
                true
            }
            //else не должен быть вызван. тк. сюда приходит лишь NoNetwork
            else false
        }.asLiveData()

    val ld = MutableLiveData<Result<List<City>>>()
    suspend fun kek(query: String) {
        findCityUseCase(query).collect {
            ld.value = it
        }
    }
//    val searchResultLiveData = _searchQuery.asStateFlow()
//        .debounce(1200)
//        .filter { it.length > 1 }
//        .mapLatest { query -> findCityUseCase(query) }
//        .map {
//            return@map if (it.succeeded) it
//            else {
//                Log.d("Tag", "error in flow THROWED")
//                throw Exception()
//            }
//        }
//        .retryWhen { cause, attempt ->
//            Log.d("Tag", "retry after $cause | $attempt ")
//            if (cause is NoNetworkException) {
//                delay(3000)
//                true
//            }
//            //else не должен быть вызван. тк. сюда приходит лишь NoNetwork
//            else false
//        }
//        .asLiveData()


    fun search(query: String) {
        _searchQuery.value = query
    }


    suspend fun addCity() {

    }

}