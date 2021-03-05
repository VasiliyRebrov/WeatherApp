package com.weather.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.data.model.HourlyWeather
import com.weather.databinding.CardHourlyWeatherItemBinding
import kotlin.math.roundToInt

class RvHourlyWeatherAdapter : RecyclerView.Adapter<RvHourlyWeatherAdapter.ViewHolder>() {
    val hourlyPresList = mutableListOf<HourlyWeather>()

    class ViewHolder(val binding: CardHourlyWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourly: HourlyWeather) {
            binding.tvDateCardHourly.text = hourly.dt
            if (hourly.pop > 0) binding.tvPopCardHourly.text = "${hourly.pop}%"
            binding.tvTempCardHourly.text = "${hourly.temp.roundToInt()}°"
            binding.imgCardHourly.setImageResource(hourly.icon)
            binding.root.setOnClickListener {
                Toast.makeText(it.context, "KEK", Toast.LENGTH_SHORT).show()
            }
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
    }

    override fun getItemCount() = hourlyPresList.size

    fun updateList(newList: List<HourlyWeather>) {  //сделать DiffUtil
        with(hourlyPresList) { clear();addAll(newList) }
        notifyDataSetChanged()
    }
}
