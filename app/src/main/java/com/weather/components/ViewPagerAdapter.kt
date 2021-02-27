package com.weather.components

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.data.model.City
import com.data.model.toRegex
import com.weather.view.CityItemFragment

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    private val cities = mutableListOf<City>()

    override fun getItemCount() = cities.size

    override fun createFragment(position: Int) =
        CityItemFragment.newInstance(cities[position].toRegex())

    fun updateList(newCityList: List<City>) {
        with(cities) { clear(); addAll(newCityList) }
        notifyDataSetChanged()
    }
}