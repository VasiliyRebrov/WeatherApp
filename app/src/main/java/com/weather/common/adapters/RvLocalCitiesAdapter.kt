package com.weather.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.data.common.createDrawablePath
import com.data.model.City
import com.data.model.CityData
import com.weather.R
import com.weather.common.components.ItemTouchHelperAdapter
import com.weather.common.components.ItemTouchHelperViewHolder
import com.weather.common.components.LocalCitiesDiffUtilCallback
import com.weather.common.components.LocalCitiesRVAdapterListener
import com.weather.databinding.CardLicalCitiesItemBinding
import java.util.*
import kotlin.math.roundToInt

class RvLocalCitiesAdapter(private val listener: LocalCitiesRVAdapterListener) :
    RecyclerView.Adapter<RvLocalCitiesAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    private val citiesList = mutableListOf<CityData>()

    //    var lastAction: ItemTouchAction = ItemTouchAction.DEFAULT
    private var blockcount = 0

    inner class ViewHolder(val binding: CardLicalCitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {
        fun bind(data: CityData) {
            binding.root.setBackgroundColor(binding.root.context.resources.getColor(R.color.colorAccent))
            binding.tvCardCityName.text = data.city.name
            binding.tvCardCityRegion.text = "${data.city.country} | ${data.city.region}"
            data.weatherData?.let {
                binding.tvCardCityTemp.text =
                    "${it.currentWeather.temp.roundToInt()} °"
            }

            val resId = data.weatherData?.currentWeather?.icon
                ?: binding.root.context.createDrawablePath("50n")
            binding.imgCardCityIcon.setImageResource(resId)

//            holder.binding.root.setOnClickListener { listener.onCitySelected(citiesList[position].city.position) }

            binding.root.setOnClickListener { listener.onItemClick(data.city.position) }
            binding.executePendingBindings()
        }

        override fun onItemSelected() {}
        override fun onItemClear() {
            with(getReorderedCities()) {
                if (isNotEmpty()) {
                    blockcount++
                    listener.reorderCities(this)
                }
            }
        }
    }

    /***
     * Возвращает новый citiesList, хранящий только те элементы, чьи 'position' были изменены
     * после swipe to delete или hold to move
     * вернуться может как много элементов, так и пустой список
     */
    fun getReorderedCities() = mutableListOf<City>().apply {
        citiesList.forEachIndexed { index, it ->
            if (it.city.position != index) {
                it.city.position = index
                add(it.city)
            }
        }
    }
//    fun getReorderedCities(): List<City> {
//        citiesList.onEachIndexed { index, it ->
//            it.city.position = index
//        }
//        return citiesList.map { it.city }
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: CardLicalCitiesItemBinding = CardLicalCitiesItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun getItemCount() = citiesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(citiesList[position])
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(citiesList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
//        lastAction = ItemTouchAction.MOVE
        return true
    }

    override fun onItemDismiss(position: Int) {
        val city = citiesList[position]
        citiesList.removeAt(position)
        notifyItemRemoved(position)
        blockcount += 2
        listener.deleteCity(city.city)
    }

    fun updateList(newList: List<CityData>) {  //сделать DiffUtil
        if (blockcount > 0)
            blockcount--
        else {
            val diffUtil = LocalCitiesDiffUtilCallback(citiesList, newList)
            val diffResult = DiffUtil.calculateDiff(diffUtil, false)
            citiesList.apply { clear(); addAll(newList) }
            diffResult.dispatchUpdatesTo(this)
        }
    }
}
