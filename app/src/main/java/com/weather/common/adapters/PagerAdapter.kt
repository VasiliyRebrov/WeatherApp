package com.weather.common.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.data.model.City
import com.weather.view.CityItemFragment


class PagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val _cities = mutableListOf<City>()
    val cities: List<City> = _cities

    override fun getCount() = _cities.size

    override fun getItem(position: Int) = CityItemFragment.newInstance(_cities[position].toString())

    override fun getItemPosition(`object`: Any) = POSITION_NONE

    fun updateList(newCityList: List<City>) {
        with(_cities) { clear(); addAll(newCityList) }
        notifyDataSetChanged()
    }
}