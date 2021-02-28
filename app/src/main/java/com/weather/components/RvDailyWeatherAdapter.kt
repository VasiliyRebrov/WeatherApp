package com.weather.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.data.model.Daily
import com.data.model.Hourly
import com.weather.databinding.CardDailyWeatherItemBinding
import com.weather.databinding.CardHourlyWeatherItemBinding
import kotlin.math.roundToInt


class RvDailyWeatherAdapter : RecyclerView.Adapter<RvDailyWeatherAdapter.ViewHolder>() {
    val dailyList = mutableListOf<Daily>()

    class ViewHolder(val binding: CardDailyWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: Daily) {
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

    fun updateList(newList: List<Daily>) {  //сделать DiffUtil
        with(dailyList) { clear();addAll(newList) }
        notifyDataSetChanged()
    }
}
