package com.data.repo

import android.content.Context
import com.data.common.Result
import com.data.model.City
import kotlinx.coroutines.flow.map

class CityItemRepo(ctx: Context) : BaseRepo(ctx) {
    fun getFlowWeatherDataByCity(cityId: Int) = dao.getFlowWeatherDataByCity(cityId)
}