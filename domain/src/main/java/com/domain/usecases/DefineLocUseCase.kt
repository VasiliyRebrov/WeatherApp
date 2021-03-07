package com.domain.usecases

import android.location.LocationListener
import android.location.LocationManager
import com.data.common.Result
import com.data.common.succeeded
import com.data.repo.AddCityRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * [DefineLocUseCase] запускает определение локации и завершается.
 * Далее, когда локация определена - [LocationListener] вызывает [AddCityByLocUseCase].
 * Интервал между этими юз-кейсами ощутим, поэтому [map] нужен для преобразования [Result.Success] в [Result.Loading],
 * для отображения статуса выполнения задачи
 * Для работы с локацией нужен главный поток, поэтому исп. [Dispatchers.Main]
 */

class DefineLocUseCase(
    private val repo: AddCityRepo,
    private val locManager: LocationManager,
    private val locListener: LocationListener,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, Unit>(coroutineDispatcher) {

    override fun execute(params: Unit): Flow<Result<Unit>> =
        repo.defineLocation(locManager, locListener).map { result ->
            return@map if (result.succeeded)
                Result.Loading
            else
                result
        }
}
