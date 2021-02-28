package com.weather.components

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.data.common.Result
import com.data.common.data
import com.data.model.City
import com.data.model.CityWeatherRelation
import com.data.model.Hourly


@JvmName("update1")
@BindingAdapter("app:update")
fun update(view: RecyclerView, result: Result<List<Hourly>>?) {
    result?.let {
        Log.d("qwerty228",it.toString())
        if (it is Result.Success) (view.adapter as RvHourlyWeatherAdapter).updateList(it.data)
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
