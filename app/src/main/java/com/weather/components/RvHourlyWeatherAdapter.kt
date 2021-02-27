package com.weather.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.data.model.Hourly
import com.weather.databinding.CardHourlyWeatherItemBinding
import kotlin.math.roundToInt

class RvHourlyWeatherAdapter : RecyclerView.Adapter<RvHourlyWeatherAdapter.ViewHolder>() {
    val hourlyPresList = mutableListOf<Hourly>()

    class ViewHolder(val binding: CardHourlyWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourly: Hourly) {
            binding.tvDateCardHourly.text=hourly.dt
            binding.tvTempCardHourly.text="${hourly.temp.roundToInt()} °"
            binding.imgCardHourly.setImageResource(hourly.icon)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: CardHourlyWeatherItemBinding =
            CardHourlyWeatherItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(hourlyPresList[position])
//        holder.binding.imageRemoteCitiesAdd.setOnClickListener {
//            listener.addCity(cities[position])
//        }
    }

    override fun getItemCount() = hourlyPresList.size

    fun updateList(newList: List<Hourly>) {  //сделать DiffUtil
        with(hourlyPresList) { clear();addAll(newList) }
        notifyDataSetChanged()
    }
}

//class RvRemoteCitiesAdapter(private val listener: Listener) :
//    RecyclerView.Adapter<RvRemoteCitiesAdapter.ViewHolder>() {
//    private val cities = mutableListOf<City>()
//
//    fun interface Listener {
//        fun addCity(city: City)
//    }
//
//    class ViewHolder(val binding: CardRemoteCitiesItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(city: City) {
//            binding.city = city
//            binding.executePendingBindings()
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val itemBinding: CardRemoteCitiesItemBinding =
//            CardRemoteCitiesItemBinding.inflate(layoutInflater, parent, false)
//        return ViewHolder(itemBinding)
//    }
//
//    override fun getItemCount() = cities.size
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(cities[position])
//        holder.binding.imageRemoteCitiesAdd.setOnClickListener {
//            listener.addCity(cities[position])
//        }
//    }
//
//    fun updateList(newList: List<City>) {  //сделать DiffUtil   ??нахер
//        cities.apply {
//            clear()
//            addAll(newList)
//        }
//        notifyDataSetChanged()
//    }
//}
