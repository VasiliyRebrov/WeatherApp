package com.weather

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.data.repo.AddCityRepo
import com.domain.FindCityByNameUseCase
import com.domain.Result
import com.domain.succeeded
import com.domain.successOr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.lang.Exception

class AddCityViewModel(application: Application, private val repo: AddCityRepo) :
    AndroidViewModel(application) {
    val findCityUseCase = FindCityByNameUseCase(repo, Dispatchers.IO)

    private val _searchQuery = MutableStateFlow("")

    //обработать именно отсутствие сети. Т.е. при отсутствии сети, попытаться снова.
    //Если ошибка "отмены корутины" - не нужно перекидывать дальше
    //возможно, в retry, вместо перезапуска и получения новой ошибки отсутствия сети,
    // делать переодически перепроверку сети.
    //или даже подписаться на получение сети
    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResultLiveData = _searchQuery.asStateFlow()
        .debounce(300)
        .filter { it.length > 1 }
        .mapLatest { query -> findCityUseCase(query) }
        .map {
            return@map if (it.succeeded) it
            else {
                Log.d("Tag", "error in flow THROWED")
                throw Exception()
            }
        }
        .retryWhen { cause, attempt ->
            Log.d("Tag", "retry after $cause | $attempt ")
            delay(3000)
            true
        }
        .asLiveData()


    fun search(query: String) {
        _searchQuery.value = query
    }
}