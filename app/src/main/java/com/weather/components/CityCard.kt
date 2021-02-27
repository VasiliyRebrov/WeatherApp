package com.weather.components

import com.data.model.City

data class CityCard(
    val city: City,
    val currentTemp: String,
    val iconStatus: Int
) {
    override fun equals(other: Any?): Boolean {
        val obj = other as? CityCard ?: return false
        return this.currentTemp == obj.currentTemp && this.iconStatus == obj.iconStatus
    }
}

