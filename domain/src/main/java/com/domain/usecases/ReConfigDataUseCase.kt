package com.domain.usecases

import com.data.common.Result
import com.data.repo.MainRepo
import com.data.repo.SettingsRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class ReConfigDataUseCase(
    private val repo: SettingsRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) :FlowUseCase<Unit,Unit>(coroutineDispatcher){
    override fun execute(parameters: Unit): Flow<Result<Unit>> {
       return repo.reConfig()
    }
}