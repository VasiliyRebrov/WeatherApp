package com.weather

import androidx.recyclerview.widget.RecyclerView
import com.data.model.City

interface MyListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder?)
    fun onStartSwipe(viewHolder: RecyclerView.ViewHolder?)
    fun sortList(cities:List<City>)
    fun deleteCityAndSort(city: City, sortedList: MutableList<City>)
}