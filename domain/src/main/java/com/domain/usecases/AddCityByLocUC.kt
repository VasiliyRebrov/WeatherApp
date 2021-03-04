package com.domain.usecases

import android.location.Location
import com.data.common.Result
import com.data.repo.AddCityRepo
import com.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

data class AddCityByLocUCParams(
    val loc: Location,
    val lang: String
)

/**
 * [AddCityByLocUC] вызывается для поиска городов по данным [Location] и добавления ближайшего.
 * Возвращает [Long] id внесенного города
 * */
class AddCityByLocUC(
    private val repo: AddCityRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlowUseCase<AddCityByLocUCParams, Long>(coroutineDispatcher) {
    override fun execute(params: AddCityByLocUCParams): Flow<Result<Long>> =
        repo.addCityByLoc(params.loc, params.lang)
}
