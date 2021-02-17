package com.weather.components

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.data.common.Result
import com.data.common.data
import com.data.common.succeeded
import com.data.model.City
import com.data.model.CityCurrentWeatherRelation


@BindingAdapter("app:update")
fun update(view: RecyclerView, result: Result<List<City>>) {
    when (result) {
        is Result.Success -> (view.adapter as RvRemoteCitiesAdapter).updateList(result.data)
        is Result.Error -> (view.adapter as RvRemoteCitiesAdapter).updateList(listOf())
    }

}

@BindingAdapter("android:text")
fun setText(view: TextView, result: Result<*>) {
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

@BindingAdapter("app:update2")
fun update2(view: RecyclerView, result: Result<List<CityCurrentWeatherRelation>>?) {
    result?.let {
        if (result is Result.Success && result.data.isNotEmpty()) {
            (view.adapter as RvLocalCitiesAdapter).updateList(result.data)
        }
    }
}


//@BindingAdapter("android:text")
//fun setText(view: TextView, error: Result<*>) {
//    if (error is Result.Error){
//        view.visibility = View.VISIBLE
//        view.text = error.exception.message
//    }
//}
//
//@BindingAdapter("android:progress")
//fun setProgress(view: TextView, isLoading: Boolean) {
//    if (isLoading) {
//        view.visibility = View.VISIBLE
//        view.text = "..."
//    } else if (view.text == "...")
//        view.visibility = View.GONE
//    //если ошибка придет раньше прогресса - она сначала отобразится, а потом прогресс исчезнет
//    //поэтому нужно убелиться, что у нас последним был активен прогресс, с его '...'
//}

//    if (progressStatus == ProgressStatus.LOADING) {
//        view.text = "..."
//        return
//    }
//    correspondence?.let {
//        view.text =
//            when {
//                it.exception != null -> it.exception.message
//                it.data == AddCityUseCases.FINDCITY -> "results"
//                else -> return
//            }
//        return
//    }
//    view.text = EMPTY_ARGS_MSG


//@BindingAdapter("app:update")
//fun update(view: RecyclerView, cities: List<CityAndCurrentWeather>?) {
//    if (!cities.isNullOrEmpty()) (view.adapter as RvLocalCitiesAdapter).updateList(cities)
////    cities?.let { (view.adapter as RvLocalCitiesAdapter).updateList(it) }
//
//}
//
//@JvmName("update1")
//@BindingAdapter("app:update")
//fun update(view: RecyclerView, cities: List<City>?) {
//    cities?.let { (view.adapter as RvRemoteCitiesAdapter).updateList(it) }
//
//}
//
//
//@BindingAdapter("app:update")
//fun update(view: ViewPager2, newList: List<City>?) {
//    newList?.let { (view.adapter as ViewPagerAdapter).updateList(newList) }
//}
//
//@BindingAdapter("android:text")
//fun setText(view: TextView, responseStatus: ResponseStatusEnum?) {
//    responseStatus?.let { view.text = it.text }
//}
//
//@BindingAdapter("android:text")
//fun setText(view: TextView, city: City?) {
//    city?.let {
//        view.text = "${it.countryCode} | ${it.region} | lat=${it.latitude} lon=${it.longitude}"
//    }
//}
//
//@BindingAdapter("app:update1")
//fun update(view: SwipeRefreshLayout,   progressStatus: ProgressStatus) {
//    view.isRefreshing = progressStatus == ProgressStatus.LOADING
//}
//
//@BindingAdapter("android:text", "android:progress", requireAll = false)
//fun setText(
//    view: TextView,
//    correspondence: Resource<AddCityUseCases>?,
//    progressStatus: ProgressStatus
//) {
//    if (progressStatus == ProgressStatus.LOADING) {
//        view.text = "..."
//        return
//    }
//    correspondence?.let {
//        view.text =
//            when {
//                it.exception != null -> it.exception.message
//                it.data == AddCityUseCases.FINDCITY -> "results"
//                else -> return
//            }
//        return
//    }
//    view.text = EMPTY_ARGS_MSG
//}
//
//@BindingAdapter("app:hiding")
//fun hiding(view: ProgressBar, progressStatus: ProgressStatus) {
//    view.visibility = if (progressStatus == ProgressStatus.LOADING) View.VISIBLE else View.GONE
//}
//
//@BindingAdapter("app:hiding")
//fun hiding(view: View, flag: Boolean) {
//    view.visibility = if (flag) View.VISIBLE else View.GONE
//}