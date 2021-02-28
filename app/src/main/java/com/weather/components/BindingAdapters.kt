package com.weather.components

import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.data.common.Result
import com.data.model.*
import com.weather.R
import kotlinx.android.synthetic.main.layout_current_weather_top.*
import kotlinx.android.synthetic.main.layout_current_weather_top.view.*
import kotlinx.android.synthetic.main.layout_grid_current_item.view.*
import kotlin.math.roundToInt


@JvmName("update1")
@BindingAdapter("app:update")
fun update(view: RecyclerView, result: Result<List<Hourly>>?) {
    result?.let {
        Log.d("qwerty228", it.toString())
        if (it is Result.Success) (view.adapter as RvHourlyWeatherAdapter).updateList(it.data)
    }
}

@BindingAdapter("app:update111")
fun update111(view: RecyclerView, result: Result<List<Daily>>?) {
    result?.let {
        if (it is Result.Success) (view.adapter as RvDailyWeatherAdapter).updateList(it.data)
    }
}

/**
 * если Success - список точно не пуст. Это проверяется еще в Retrofit-util.
 * Если список оказывается пуст - вернется Error
 * */
@BindingAdapter("app:update228")
fun update228(view: RecyclerView, result: Result<List<City>>) {
    when (result) {
        is Result.Success -> (view.adapter as RvRemoteCitiesAdapter).updateList(result.data)
        is Result.Error -> (view.adapter as RvRemoteCitiesAdapter).updateList(listOf())
    }

}

//изначально всегда придет null, даже если в LD не может храниться null
//дело в отсутствии привязки к лд в начале
@BindingAdapter("app:update")
fun update(view: GridLayout, result: Result<CurrentWeatherData>?) {
    fun View.initCell(value: String, name: String) {
        this.img_grid_item.setImageResource(R.drawable._10d)
        this.tv_grid_item_value.text = value
        this.tv_grid_item_name.text = name
    }
    result?.let {
        if (result is Result.Success) {
            with(result.data) {
                view[0].initCell("$humidity %", "влажность")
                view[1].initCell("$uvi", "УФ")
                view[2].initCell("$wind_speed", "скорость ветра")
                view[3].initCell("$dew_point", "точка росы")
                view[4].initCell("$visibility km", "видимость")
                view[5].initCell("$pressure ", "давление")
            }
        }
    }
}

//android:id="@+id/tv_current_weather_temp"
//android:id="@+id/tv_current_weather_status"
//android:id="@+id/tv_current_weather_feels"
@BindingAdapter("app:update")
fun update(view: ConstraintLayout, result: Result<CurrentWeatherData>?) {
    result?.let {
        if (it is Result.Success) {
            with(it.data) {
                view.tv_current_weather_temp.text = "${temp.roundToInt()}°"
                view.tv_current_weather_feels.text = "ощущается как ${feels_like.roundToInt()}"
                view.tv_current_weather_status.text = description
            }
        }
    }
}

/**
 * если Success - список может быть пуст. (предположение)
 * */
@BindingAdapter("app:update")
fun update(view: ViewPager2, result: Result<List<City>>?) {
    result?.let {
        if (it is Result.Success && it.data.isNotEmpty()) {
            (view.adapter as ViewPagerAdapter).updateList(it.data)
        }
    }
}

/**
 * в начале приходит null
 * если Success - список может быть пуст, добавлена проверка
 * */
@BindingAdapter("app:update2")
fun update2(view: RecyclerView, result: Result<List<CityWeatherRelation>>?) {
    result?.let {
        if (it is Result.Success && it.data.isNotEmpty())
            (view.adapter as RvLocalCitiesAdapter).updateList(it.data)
    }
}

/**
 * result - это baseLD, он медиатор, и он будет null до тех пор, пока какаой-либо из юзкейсов
 * не выполнится. Здесь не попадался null, это потому, что юзкейс по считыванию EditText
 * активируется быстро. Быстрее, чем TextView начинает обращаться к BaseLD.
 * Но на всякий случай указал возможность null.
 * В будущем, возможно, будет лучше механизм baseLD
 * */
@BindingAdapter("android:text")
fun setText(view: TextView, result: Result<*>?) {
    when (result) {
        is Result.Success -> {
            view.visibility = View.GONE
        }
        is Result.Loading -> {
            view.visibility = View.VISIBLE
            view.text = "..."
        }
        is Result.Error -> {
            view.visibility = View.VISIBLE
            view.text = result.exception.message
        }
    }
}

/**
 * result - это baseLD, он медиатор, и он будет null до тех пор, пока какаой-либо из юзкейсов
 * не выполнится
 * Здесь изначально result - null. И до тех пор, пока юзкейс - swipeToRefresh Weather Data не будет
 * выполнен.
 * В будущем, возможно, будет лучше механизм baseLD
 * */
@BindingAdapter("app:progress")
fun progress(view: SwipeRefreshLayout, result: Result<*>?) {
    result?.let {
        view.isRefreshing = result is Result.Loading

    }
}
