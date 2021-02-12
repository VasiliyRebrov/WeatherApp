package com.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.data.common.MediatorSingleLiveEvent
import com.data.common.Result
import com.data.repo.BaseRepo
import com.domain.FlowUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

    /** это ливдата отображения прогресса.
     * она слушает все ливдаты в контейнере*/
    private val _progressLiveData by lazy {
        MediatorLiveData<Boolean>().apply {
            liveDataContainer.forEach { liveData ->
                addSource(liveData) { result -> switchProgress(result) }
            }
        }
    }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    /** это ливдата отображения ошибок.
     * она слушает все ливдаты в контейнере*/
    private val _errorEvent by lazy {
        MediatorLiveData<Result<*>>().apply {
            liveDataContainer.forEach { liveData ->
                addSource(liveData) { result -> setError(result) }
            }
        }
    }
    val errorEvent: LiveData<Result<*>> by lazy { _errorEvent }

    /** фабричный метод по инициализации контейнера ливдат*/
    protected abstract fun initLiveDataContainer(): Set<LiveData<Result<*>>>

    /** утилитарные методы-расширения для верхний ливдат*/
    private fun MediatorLiveData<Boolean>.switchProgress(source: Result<*>) {
        value = source is Result.Loading
    }

    private fun MediatorLiveData<Result<*>>.setError(source: Result<*>) {
        value = source
//        if (source is Result.Error) value = source
//        else value=null
    }

    /** метод для запуска юз-кейсов, возвращающих Flow
    корутина начинает работу в Main-потоке. Но возможнсть смены предусмотрена в юз-кейсе*/
    protected fun <P, R> launchUseCase(
        useCase: FlowUseCase<P, R>,
        parameters: P,
        collectorBlock: suspend (Result<R>) -> Unit
    ) {
        viewModelScope.launch { useCase(parameters).collect(collectorBlock) }
    }
}

/**
 * есть аналогичное решение, но не факт, что оно лучше:
 * не надо подписываться на ливдаты субкласса сразу, при инициализации _progress & _error
 * вместо этого, в субклассе, когда все ливдаты будут инициализированы, просто вызвать метод родителя
 * передав ему все ливдаты
 * этот метод инкапсулирует подписку
 * не нужно никаких хранилищ ливдаты и вообще, кода меньше
 * ___
 * с другой стороны, нарушается 'голливудский принцип'
 * одинаковое поведение, стартующее субклассами -
 * это значит, что обязательный вызов метода должен осуществляться во всех субклассах - это плохое решение
 * пусть родитель определяет вызовы, а субклассы лишь реализуют необходимые методы.
 * т.е. ничего менять не надо
 *
 * */

