package com.domain.usecases

import com.data.common.Result
import com.data.repo.SettingsRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * [TransformDataUC] преобразует локальные данные, редактируя их значения
 * относительно нового установленного значения единиц измерения
 * */
class TransformDataUC(
    private val repo: SettingsRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<String, Int>(coroutineDispatcher) {
    override fun execute(params: String): Flow<Result<Int>> = repo.transformData(params)
}