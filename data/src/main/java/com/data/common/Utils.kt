package com.data.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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

fun createWeatherEntity(
    cityId: Int,
    weather: WeatherResponsePOJO
): Triple<CurrentWeatherData, HourlyWeatherData, DailyWeatherData> {
    val currentWeather = CurrentWeatherData.createFromRemoteEntity(cityId, weather.current)
    val hourlyWeather = HourlyWeatherData.createFromRemoteEntity(cityId, weather.hourly)
    val dailyWeather = DailyWeatherData.createFromRemoteEntity(cityId, weather.daily)
    return Triple(currentWeather, hourlyWeather, dailyWeather)
}
