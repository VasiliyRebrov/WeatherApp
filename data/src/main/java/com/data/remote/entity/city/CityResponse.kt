package com.data.remote.entity.city

import com.data.model.City

data class CityResponse(
    val data: List<City>,
    val metadata: Metadata
)