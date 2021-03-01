package com.weather.common.components

import androidx.recyclerview.widget.RecyclerView
import com.data.model.City

/**
 * интерфейс для взаимодействия RecyclerView адаптера и View
 * */
interface MyListener {

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder?)
    fun onStartSwipe(viewHolder: RecyclerView.ViewHolder?)

    /**
     * у удаляем конкретный город  (в будущем будут города)
     * */
    fun deleteCity(city: City)

    /**
     * отправляем список с городами, чьи позиции были изменены
     * */
    fun reorderLocalCities(reorderedCities: List<City>)
}