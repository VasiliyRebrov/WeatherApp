package com.weather.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.data.model.City
import com.data.model.CityCurrentWeatherRelation
import com.weather.databinding.CardLicalCitiesItemBinding
import java.util.*

class RvLocalCitiesAdapter(private val mDragStartListener: MyListener) :
    RecyclerView.Adapter<RvLocalCitiesAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    private val citiesList = mutableListOf<CityCurrentWeatherRelation>()

    //    var lastAction: ItemTouchAction = ItemTouchAction.DEFAULT
    private var blockFlag = false

    inner class ViewHolder(private val binding: CardLicalCitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {
        fun bind(data: CityCurrentWeatherRelation) {
            binding.data = data
            binding.executePendingBindings()
        }

        override fun onItemSelected() {}
        override fun onItemClear() {
            with(getReorderedCities()) {
                if (isNotEmpty()) {
                    blockFlag = true
                    mDragStartListener.reorderLocalCities(this)
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
        blockFlag = true
        mDragStartListener.deleteCity(city.city)
    }

    fun updateList(newList: List<CityCurrentWeatherRelation>) {  //сделать DiffUtil
        if (blockFlag)
            blockFlag = false
        else {
            val diffUtil = LocalCitiesDiffUtilCallback(citiesList, newList)
            val diffResult = DiffUtil.calculateDiff(diffUtil, false)
            citiesList.apply { clear(); addAll(newList) }
            diffResult.dispatchUpdatesTo(this)
        }
    }
}
