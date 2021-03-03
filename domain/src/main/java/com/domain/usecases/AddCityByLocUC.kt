package com.domain.usecases

import android.location.Location
import com.data.common.Result
import com.data.repo.AddCityRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class AddCityByLocUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Location, Int>(coroutineDispatcher) {
    override fun execute(params: Location): Flow<Result<Int>> {
        return repo.addCityByLoc(params)
    }
}
