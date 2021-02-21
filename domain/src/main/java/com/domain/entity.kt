package com.domain

import com.data.model.City

data class RefreshWeatherParams(
    val unitMeasurePref: String,
    val newCities: List<City>,
    val oldCities: List<City> = listOf()
)