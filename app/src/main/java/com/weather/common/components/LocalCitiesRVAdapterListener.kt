package com.weather.common.components

import androidx.recyclerview.widget.RecyclerView
import com.data.model.City

/**
 * интерфейс для взаимодействия RecyclerView адаптера и View
 * */
interface LocalCitiesRVAdapterListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder?)
    fun onStartSwipe(viewHolder: RecyclerView.ViewHolder?)

    fun onDeleteCity(city: City)
    fun onReorderCities(reorderedCities: List<City>)
    fun onItemClick(position: Int)
}