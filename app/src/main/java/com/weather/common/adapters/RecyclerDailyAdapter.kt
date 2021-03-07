package com.weather.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.data.model.DailyWeather
import com.weather.databinding.CardDailyItemBinding
import kotlin.math.roundToInt


class RecyclerDailyAdapter : RecyclerView.Adapter<RecyclerDailyAdapter.ViewHolder>() {
    val dailyList = mutableListOf<DailyWeather>()

    class ViewHolder(val binding: CardDailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: DailyWeather) {
            binding.tvCardDailyItemDate.text = daily.dt
            binding.imgCardDailyItemStatus.setImageResource(daily.icon)
            binding.tvCardDailyItemMinTemp.text = "${daily.tempMin.roundToInt()} °"
            binding.tvCardDailyItemMaxTemp.text = "${daily.tempMax.roundToInt()} °"
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: CardDailyItemBinding =
            CardDailyItemBinding.inflate(layoutInflater, parent, false)
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
