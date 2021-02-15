package com.weather.components

import androidx.recyclerview.widget.DiffUtil
import com.data.model.CityAndCurrentWeather

class LocalCitiesDiffUtilCallback(
    private val oldCities: List<CityAndCurrentWeather>,
    private val newCities: List<CityAndCurrentWeather>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldCities.size

    override fun getNewListSize() = newCities.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldCities[oldItemPosition].city.cityId == newCities[newItemPosition].city.cityId


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldCities[oldItemPosition].currentWeatherData == newCities[newItemPosition].currentWeatherData


}