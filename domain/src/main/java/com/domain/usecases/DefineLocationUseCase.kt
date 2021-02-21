package com.domain.usecases

import android.location.LocationListener
import android.location.LocationManager
import com.data.common.Result
import com.data.common.succeeded
import com.data.repo.AddCityRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefineLocationUseCase(
    private val repo: AddCityRepo,
    private val locManager: LocationManager,
    private val locListener: LocationListener,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, Unit>(coroutineDispatcher) {
    /** используем маппинг, преобразуя успешный результат в результат загрузки
     * причина: когда успешно прекращается данный юзкейс, автоматически начинается другой - добавление города
     *но интервал между окончанием выполнения этого юзкейса и началом выполнения нового - он очень велик
     * потому что мы ждем, когда найдется локация и location listener начнет новый юзкейс
     * этот интервал ожидания должен сопровождаться загрузкой*/
    override fun execute(params: Unit): Flow<Result<Unit>> {
        return repo.defineLocation(locManager, locListener).map { result ->
            return@map if (result.succeeded) Result.Loading
            else result
        }
    }
}
