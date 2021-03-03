package com.data.repo

import android.content.Context

class CityItemRepo(ctx: Context) : BaseRepo(ctx) {
    fun getFlowWeatherDataByCity(cityId: Int) = dao.getFlowDataById(cityId)
}