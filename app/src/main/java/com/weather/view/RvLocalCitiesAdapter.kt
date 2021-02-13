package com.weather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.data.model.City
import com.data.model.CityAndCurrentWeather
import com.weather.ItemTouchHelperAdapter
import com.weather.ItemTouchHelperViewHolder
import com.weather.MyListener
import com.weather.databinding.CardLicalCitiesItemBinding
import java.util.*

class RvLocalCitiesAdapter(private val mDragStartListener: MyListener) :
    RecyclerView.Adapter<RvLocalCitiesAdapter.ViewHolder>(), ItemTouchHelperAdapter {
    private val cities = mutableListOf<CityAndCurrentWeather>()
    var lastAction: ItemTouchAction = ItemTouchAction.DEFAULT

    inner class ViewHolder(val binding: CardLicalCitiesItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ), ItemTouchHelperViewHolder {
        fun bind(data: CityAndCurrentWeather) {
            binding.data = data
            binding.executePendingBindings()
        }

        override fun onItemSelected() {
        }

        override fun onItemClear() {
            if (lastAction == ItemTouchAction.MOVE) {
                mDragStartListener.sortList(resort())
            }
        }
    }

    fun resort(): MutableList<City> {
        val list = mutableListOf<City>()
        cities.forEachIndexed { index, it ->
            it.city.serialNumber = index
            list.add(it.city)
        }
        return list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: CardLicalCitiesItemBinding = CardLicalCitiesItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    fun updateList(newList: List<CityAndCurrentWeather>) {  //сделать DiffUtil
        if (lastAction != ItemTouchAction.DEFAULT) {
            lastAction = ItemTouchAction.DEFAULT
        } else {
            val diffUtil = LocalCitiesDiffUtilCallback(cities, newList)
            val diffResult = DiffUtil.calculateDiff(diffUtil, false)
            cities.apply { clear(); addAll(newList) }
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(cities, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        lastAction = ItemTouchAction.MOVE
        return true
    }

    override fun onItemDismiss(position: Int) {
        val city = cities[position]
        cities.removeAt(position)
        notifyItemRemoved(position)
        lastAction = ItemTouchAction.SWIPE
        val sortedList=resort()
        mDragStartListener.deleteCityAndSort(city.city,sortedList)
    }
}
