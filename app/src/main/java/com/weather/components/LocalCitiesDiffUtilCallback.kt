package com.weather.components

import androidx.recyclerview.widget.DiffUtil

class LocalCitiesDiffUtilCallback(
    private val oldCities: List<CityCard>,
    private val newCities: List<CityCard>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldCities.size

    override fun getNewListSize() = newCities.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldCities[oldItemPosition].city.cityId == newCities[newItemPosition].city.cityId


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCities[oldItemPosition] == newCities[newItemPosition]
    }
}