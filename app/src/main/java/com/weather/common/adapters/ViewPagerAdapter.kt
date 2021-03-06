package com.weather.common.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.data.model.City
import com.data.model.toRegex
import com.weather.view.CityItemFragment


class ViewPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val cities = mutableListOf<City>()

    override fun getCount() = cities.size

    override fun getItem(position: Int) = CityItemFragment.newInstance(cities[position].toRegex())
    override fun getPageTitle(position: Int) = cities[position].name

    override fun getItemPosition(`object`: Any) = POSITION_NONE


    fun updateList(newCityList: List<City>) {
        with(cities) { clear(); addAll(newCityList) }
        notifyDataSetChanged()
    }
}