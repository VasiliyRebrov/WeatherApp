package com.data.common

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.data.model.*
import com.data.remote.entity.WeatherResponsePOJO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

fun CurrentWeatherData.transformData(unitMeasurePref: String) {
    val tempCalculator = getTempCalculator(unitMeasurePref)
    val windCalculator = getWindCalculator(unitMeasurePref)
    this.temp = roundAvoid(tempCalculator(temp), 2)
    this.feels_like = roundAvoid(tempCalculator(feels_like), 2)
    this.wind_speed = roundAvoid(windCalculator(wind_speed), 2)
}

fun Hourly.transformData(unitMeasurePref: String) {
    val tempCalculator = getTempCalculator(unitMeasurePref)
    val windCalculator = getWindCalculator(unitMeasurePref)
    this.temp = roundAvoid(tempCalculator(temp), 2)
    this.feels_like = roundAvoid(tempCalculator(feels_like), 2)
    this.wind_speed = roundAvoid(windCalculator(wind_speed), 2)
}

fun Daily.transformData(unitMeasurePref: String) {
    val tempCalculator = getTempCalculator(unitMeasurePref)
    val windCalculator = getWindCalculator(unitMeasurePref)
    this.tempDay = roundAvoid(tempCalculator(tempDay), 2)
    this.tempEve = roundAvoid(tempCalculator(tempEve), 2)
    this.tempMax = roundAvoid(tempCalculator(tempMax), 2)
    this.tempMin = roundAvoid(tempCalculator(tempMin), 2)
    this.tempMorn = roundAvoid(tempCalculator(tempMorn), 2)
    this.tempNight = roundAvoid(tempCalculator(tempNight), 2)
    this.wind_speed = roundAvoid(windCalculator(wind_speed), 2)
}

fun getTempCalculator(unitMeasurePref: String): (Double) -> Double =
    if (unitMeasurePref == "Imperial") { temp -> temp * 9 / 5 + 32 }
    else { temp -> (temp - 32) * 5 / 9 }

fun getWindCalculator(unitMeasurePref: String): (Double) -> Double =
    if (unitMeasurePref == "Imperial") { wind -> wind * 2.237 }
    else { wind -> wind / 2.237 }

fun roundAvoid(value: Double, places: Int): Double {
    val scale = 10.0.pow(places.toDouble())
    return (value * scale).roundToInt() / scale
}

fun createDate(date: Int): String {
    val unix = "${date}000".toLong()
    val requireFormat =
        SimpleDateFormat("HH:mm", Locale.getDefault()) // паттерн в константу
    val _date = Date(unix)
    return requireFormat.format(_date)
}

fun checkInternetAccess(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun Context.getDrawablePath(name: String) =
    resources.getIdentifier("_$name", "drawable", packageName)

fun Context.mapRemoteWeatherToEntity(
    cityId: Int,
    remoteWeather: WeatherResponsePOJO
): WeatherData {
    fun createCurrent() = with(remoteWeather.current) {
        CurrentWeatherData(
            clouds,
            dew_point,
            createDate(dt),
            feels_like,
            humidity,
            pressure,
            createDate(sunrise),
            createDate(sunset),
            temp,
            uvi,
            visibility,
            wind_deg,
            wind_speed,
            weather[0].description,
            getDrawablePath(weather[0].icon)
        )
    }


    fun createHourly() = remoteWeather.hourly.map {
        Hourly(
            it.clouds,
            it.dew_point,
            createDate(it.dt),
            it.feels_like,
            it.humidity,
            it.pop,
            it.pressure,
            it.rain?.`1h` ?: 228.0,
            it.temp,
            it.uvi,
            it.visibility,
            it.weather[0].description,
            getDrawablePath(it.weather[0].icon),
            it.wind_deg,
            it.wind_speed
        )
    }

    fun createDaily() = remoteWeather.daily.map {
        Daily(
            it.clouds,
            it.dew_point,
            createDate(it.dt),
            it.humidity,
            it.pop,
            it.pressure,
            it.rain,
            it.snow,
            createDate(it.sunrise),
            createDate(it.sunset),
            it.temp.day,
            it.temp.eve,
            it.temp.max,
            it.temp.min,
            it.temp.morn,
            it.temp.night,
            it.uvi,
            it.weather[0].description,
            getDrawablePath(it.weather[0].icon),
            it.wind_deg,
            it.wind_speed
        )
    }
    return WeatherData(cityId, createCurrent(), createHourly(), createDaily())
}







