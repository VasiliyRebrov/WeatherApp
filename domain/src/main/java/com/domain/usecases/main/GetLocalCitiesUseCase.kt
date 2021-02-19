package com.domain.usecases.main

import android.util.Log
import com.data.common.Result
import com.data.model.City
import com.data.repo.MainRepo
import com.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLocalCitiesUseCase(
    private val repo: MainRepo,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : FlowUseCase<Unit, List<City>>(coroutineDispatcher) {
    /** полученный список городов сортируется по id.*/
    /** сортировка по id позволяет исключить изменения, после юзкейса пересортировки,
     * когда меняется поле pos */
    override fun execute(parameters: Unit): Flow<Result<List<City>>> {
        return repo.localCities.map { resultList ->
            Result.Success(resultList.sortedBy { city -> city.cityId })
        }
    }
}

