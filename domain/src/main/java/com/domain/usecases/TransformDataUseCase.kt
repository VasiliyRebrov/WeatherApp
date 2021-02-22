package com.domain.usecases

import com.data.common.Result
import com.data.repo.SettingsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class TransformDataUseCase(
    private val repo: SettingsRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<String, Unit>(coroutineDispatcher) {
    override fun execute(params: String): Flow<Result<Unit>> {
        return repo.transformData(params)
    }
}