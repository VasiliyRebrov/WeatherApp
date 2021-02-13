package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.Result
import com.data.common.SingleLiveEvent
import com.data.repo.BaseRepo
import com.domain.FlowUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

abstract class BaseViewModel(application: Application, private val repo: BaseRepo) :
    AndroidViewModel(application) {
    /** все нижние объекты имеют ленивую инициализацию
     * Дело в том, что их инициализация зависит от ливдат в субклассах,
     * но объекты субклассов инициализируются после родительских.
     * Решение: тк здесь объекты Lazy - они будут созданы при подписке на них
     * а объекты-ливдаты субклассов будут созданы сразу
     **/

    /** это контейнер всех ливдат, которые обрабатывают юзкейсы*/
    private val liveDataContainer: Set<LiveData<Result<*>>> by lazy { initLiveDataContainer() }


    /** фабричный метод по инициализации контейнера ливдат*/
    protected abstract fun initLiveDataContainer(): Set<LiveData<Result<*>>>

    private val _baseLiveData by lazy {
        MediatorLiveData<Result<*>>().apply {
            liveDataContainer.forEach { liveData ->
                addSource(liveData) { result ->
                    value = result
                    if (result is Result.Error) _errorEvent.value = result.exception
                }
            }
        }
    }
    val baseLiveData: LiveData<Result<*>> by lazy { _baseLiveData }

    private val _errorEvent = SingleLiveEvent<Exception>()
    val errorEvent: LiveData<Exception> = _errorEvent

    /** метод для запуска юз-кейсов, возвращающих Flow
    корутина начинает работу в Main-потоке. Но возможнсть смены предусмотрена в юз-кейсе*/
    protected fun <P, R> launchUseCase(
        useCase: FlowUseCase<P, R>,
        parameters: P,
        collect: suspend (Result<R>) -> Unit
    ) {
        viewModelScope.launch { useCase(parameters).collect(collect) }
    }
}

/**
 * возможно, сделать shared/state flow.
 * */