package com.weather.common.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.data.model.City
import com.weather.databinding.CardRemoteCitiesItemBinding

class RvRemoteCitiesAdapter(private val listener: Listener) :
    RecyclerView.Adapter<RvRemoteCitiesAdapter.ViewHolder>() {
    private val cities = mutableListOf<City>()

    fun interface Listener {
        fun addCity(city: City)
    }

    class ViewHolder(val binding: CardRemoteCitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            binding.city = city
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: CardRemoteCitiesItemBinding =
            CardRemoteCitiesItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cities[position])
        holder.binding.imageRemoteCitiesAdd.setOnClickListener {
            listener.addCity(cities[position])
        }
    }

    fun updateList(newList: List<City>) {  //сделать DiffUtil   ??нахер
        cities.apply {
            clear()
            addAll(newList)
        }
        notifyDataSetChanged()
    }
}
