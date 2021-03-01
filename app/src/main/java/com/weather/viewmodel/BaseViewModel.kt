package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.Result
import com.data.common.SingleLiveEvent
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: Application) :
    AndroidViewModel(application) {
    /** все нижние объекты имеют ленивую инициализацию
     * Дело в том, что их инициализация зависит от ливдат в субклассах,
     * но объекты субклассов инициализируются после родительских.
     * Решение: тк здесь объекты Lazy - они будут созданы при подписке на них
     * а объекты-ливдаты субклассов будут созданы сразу
     **/

    /** это контейнер всех ливдат, которые обрабатывают юзкейсы*/

    abstract val useCases: Map<String, LiveData<Result<*>>>

    /** фабричный метод по инициализации контейнера ливдат*/

    /***
     * обращайся сюда, если результат выполнения юзкейсов являет состояние
     */
    private val _baseLD by lazy {
        MediatorLiveData<Result<*>>().apply {
            useCases.forEach { entry ->
                addSource(entry.value) { result ->
                    value = result
                    //мб пусть ивент подписывается на базу?
                    if (result is Result.Error) _errorEvent.value = result
                }
            }
        }
    }
    val baseLD: LiveData<Result<*>> by lazy { _baseLD }

    /** event handler для события юзкейсов - успех. неудача
     * обращайся сюда, если результат выполнения юзкейсов являет событие
     * */
    private val _errorEvent = SingleLiveEvent<Result.Error>()
    val errorEvent: LiveData<Result.Error> = _errorEvent

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