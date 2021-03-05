package com.weather.common.components

import androidx.recyclerview.widget.DiffUtil
import com.data.model.CityData

class LocalCitiesDiffUtilCallback(
    private val oldCities: List<CityData>,
    private val newCities: List<CityData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldCities.size

    override fun getNewListSize() = newCities.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldCities[oldItemPosition].city.cityId == newCities[newItemPosition].city.cityId


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldCities[oldItemPosition]
        val newItem = newCities[newItemPosition]
        return oldItem.weatherData?.currentWeather?.temp == newItem.weatherData?.currentWeather?.temp &&
                oldItem.weatherData?.currentWeather?.icon == newItem.weatherData?.currentWeather?.icon
    }
}