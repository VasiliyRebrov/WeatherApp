package com.weather.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.data.model.DailyWeather
import com.weather.databinding.CardDailyWeatherItemBinding
import kotlin.math.roundToInt


class RvDailyWeatherAdapter : RecyclerView.Adapter<RvDailyWeatherAdapter.ViewHolder>() {
    val dailyList = mutableListOf<DailyWeather>()

    class ViewHolder(val binding: CardDailyWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: DailyWeather) {
            binding.tvCardDailyDate.text = daily.dt
            binding.imgCardDailyStatus.setImageResource(daily.icon)
            binding.tvCardDailyMinTemp.text = "${daily.tempMin.roundToInt()} °"
            binding.tvCardDailyMaxTemp.text = "${daily.tempMax.roundToInt()} °"
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: CardDailyWeatherItemBinding =
            CardDailyWeatherItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyList[position])
//        holder.binding.imageRemoteCitiesAdd.setOnClickListener {
//            listener.addCity(cities[position])
//        }
    }

    override fun getItemCount() = dailyList.size

    fun updateList(newList: List<DailyWeather>) {  //сделать DiffUtil
        with(dailyList) { clear();addAll(newList) }
        notifyDataSetChanged()
    }
}
