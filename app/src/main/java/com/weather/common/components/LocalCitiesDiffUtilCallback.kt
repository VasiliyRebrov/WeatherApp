package com.weather.common.components

import androidx.recyclerview.widget.DiffUtil
import com.data.model.CityWeatherRelation

class LocalCitiesDiffUtilCallback(
    private val oldCities: List<CityWeatherRelation>,
    private val newCities: List<CityWeatherRelation>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldCities.size

    override fun getNewListSize() = newCities.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldCities[oldItemPosition].city.cityId == newCities[newItemPosition].city.cityId


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldCities[oldItemPosition]
        val newItem = newCities[newItemPosition]
        return oldItem.weatherData?.currentWeatherData?.temp == newItem.weatherData?.currentWeatherData?.temp &&
                oldItem.weatherData?.currentWeatherData?.icon == newItem.weatherData?.currentWeatherData?.icon
    }
}