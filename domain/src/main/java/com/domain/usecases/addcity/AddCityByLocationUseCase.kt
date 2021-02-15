package com.domain.usecases.addcity

import android.location.Location
import com.data.common.Result
import com.data.repo.AddCityRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class AddCityByLocationUseCase(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Location, Int>(coroutineDispatcher) {
    override fun execute(parameters: Location): Flow<Result<Int>> {
        return repo.addCityByLocation(parameters)
    }
}
