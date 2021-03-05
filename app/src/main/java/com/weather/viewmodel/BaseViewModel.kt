package com.weather.viewmodel

import android.app.Application
import android.os.Build
import android.os.LocaleList
import androidx.lifecycle.*
import com.data.common.Result
import com.data.common.SingleLiveEvent
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    /** [currentLang] дает текущую конфиг языка*/
    val currentLang: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            LocaleList.getDefault().get(0).language
        else
            Locale.getDefault().language

    /** [useCases] - это контейнер всех ливдат текущего субкласса, которые обрабатывают юзкейсы.*/
    abstract val useCases: Map<String, LiveData<Result<*>>>

    /**
     * [baseUCLD] - принимает всез начения [useCases]. Обращайся сюда, если результат [LiveData] являет состояние
     */
    val baseUCLD: LiveData<Result<*>> by lazy {
        MediatorLiveData<Result<*>>().apply {
            useCases.forEach { entry ->
                addSource(entry.value) { result ->
                    value = result
                    if (result is Result.Error) _errorEvent.value = result
                }
            }
        }
    }

    /**
     * [_errorEvent] - принимает все значения [useCases], где результат [Result.Error].
     * Обращайся сюда, если результат [LiveData] являет событие
     * */
    private val _errorEvent by lazy { SingleLiveEvent<Result.Error>() }
    val errorEvent: LiveData<Result.Error> by lazy { _errorEvent }

    /** [launchUseCase] - метод для запуска юз-кейсов, релазаций [FlowUseCase]*/
    protected fun <P, R> launchUseCase(
        useCase: FlowUseCase<P, R>,
        parameters: P,
        collect: suspend (Result<R>) -> Unit
    ) {
        viewModelScope.launch { useCase(parameters).collect(collect) }
    }
}