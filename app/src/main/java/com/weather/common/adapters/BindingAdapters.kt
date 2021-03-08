package com.weather.common.adapters

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.data.common.*
import com.data.model.*
import com.weather.R
import com.weather.common.entities.Config.Companion.getWindUnits
import kotlinx.android.synthetic.main.layout_current_data_top.view.*
import kotlinx.android.synthetic.main.layout_grid_current_item.view.*
import kotlin.math.roundToInt

@BindingAdapter("app:update")
fun updateGeneralPager(view: ViewPager, result: Result<List<City>>?) {
    result?.let {
        if (it is Result.Success && it.data.isNotEmpty()) {
            (view.adapter as PagerAdapter).updateList(it.data)
        }
    }
}

/** если [Result.Success] - список точно не пуст. Это проверяется еще в Retrofit-util.*/
@BindingAdapter("app:update_remote_rv")
fun updateRemoteRecycler(view: RecyclerView, result: Result<List<City>>) {
    if (result is Result.Success) {
        view.visibility = View.VISIBLE
        (view.adapter as RecyclerRemoteCitiesAdapter).updateList(result.data)
    } else view.visibility = View.GONE
}

@BindingAdapter("app:update")
fun updateCurrentTop(view: ConstraintLayout, result: Result<CurrentWeather>?) {
    result?.let {
        if (it is Result.Success) {
            with(it.data) {
                view.tv_current_data_top_temp.text = "${temp.roundToInt()}°"
                view.tv_current_data_top_feels.text = "ощущается как ${feels_like.roundToInt()}"
                view.tv_current_data_top_status.text = description
            }
        }
    }
}

@BindingAdapter("app:update_hourly_rv")
fun updateHourlyRecycler(view: RecyclerView, result: Result<List<HourlyWeather>>?) {
    result?.let {
        if (it is Result.Success) (view.adapter as RecyclerHourlyAdapter).updateList(it.data)
    }
}

@BindingAdapter("app:update_daily_rv")
fun updateDailyRecycler(view: RecyclerView, result: Result<List<DailyWeather>>?) {
    result?.let {
        if (it is Result.Success) (view.adapter as RecyclerDailyAdapter).updateList(it.data)
    }
}

@BindingAdapter("app:update")
fun updateCitiesManagerRecycler(view: RecyclerView, result: Result<List<CityData>>?) {
    result?.let {
        if (it is Result.Success && it.data.isNotEmpty())
            (view.adapter as RvLocalCitiesAdapter).updateList(it.data)
    }
}


@BindingAdapter("app:update")
fun update(view: Button, result: Result<*>?) {
    result?.let {
        view.visibility =
            if (it is Result.Error && it.exception.cause is NoNetworkException)
                View.VISIBLE
            else
                View.GONE
    }
}


@BindingAdapter("app:update")
fun updateGrid(view: GridLayout, result: Result<CurrentWeather>?) {
    fun View.initCell(value: String, name: String) {
        this.img_grid_item_type.setImageResource(R.drawable._50d)
        this.tv_grid_item_value.text = value
        this.tv_grid_item_name.text = name
    }
    result?.let {
        if (result is Result.Success) {
            with(result.data) {
                view[0].initCell(
                    view.resources.getString(R.string.humidity_value, "$humidity%"),
                    "Относительная влажность"
                )
                view[1].initCell(uvi.toString(), "УФ индекс")
                view[2].initCell(
                    view.resources.getString(
                        R.string.wind_speed_value,
                        wind_speed.roundToInt(),
                        view.context.getWindUnits()
                    ),
                    "Скорость ветра"
                )
                view[3].initCell(
                    view.resources.getString(R.string.temp_value, dew_point.roundToInt()),
                    "Точка росы"
                )
                view[4].initCell(
                    view.resources.getString(R.string.visibility_value, visibility),
                    "Видимость"
                )
                view[5].initCell(
                    view.resources.getString(R.string.pressure_value, pressure),
                    "Давление"
                )
            }
        }
    }
}

@BindingAdapter("app:progress")
fun progress(view: SwipeRefreshLayout, result: Result<*>?) {
    result?.let {
        view.isRefreshing = result is Result.Loading
    }
}

@BindingAdapter("android:text")
fun setText(view: TextView, result: Result<*>?) {
    view.text = when {
        (result is Result.Loading) -> "Поиск..."
        (result is Result.Error) -> {
            if (result.exception.cause is NetworkProviderDisabledException
                ||
                result.exception.cause is CityAlreadyExistException
            )
                INVALID_ARGS_MSG
            else
                result.exception.cause!!.message
        }
        else -> ""
    }
}