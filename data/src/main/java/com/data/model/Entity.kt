package com.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.data.common.SEPARATOR
import com.data.common.createDate
import com.data.remote.entity.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cities")
data class City(
    @PrimaryKey
    @SerializedName("id")
    val cityId: Int,
    val city: String,
    val country: String,
    val countryCode: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val region: String,
    val regionCode: String,
    var position: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        val obj = other as? City ?: return false
        return this.cityId == obj.cityId
    }

    companion object {
        fun createCityByRegex(cityRegex: String) = with(cityRegex.split(SEPARATOR)) {
            City(
                this[0].toInt(), this[1], this[2], this[3], this[4],
                this[5], this[6], this[7], this[8], this[9].toInt()
            )
        }
    }

}

@Entity(tableName = "current_weather_data")
data class CurrentWeatherData(
    @PrimaryKey
    val cityId: Int,
    val clouds: Int,
    val dew_point: Double,
    val dt: String,
    var feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: String,
    val sunset: String,
    var temp: Double,
    val uvi: Double,
    val visibility: Int,
    val wind_deg: Int,
    var wind_speed: Double,
    val description: String,
    val icon: String,
) : WeatherEntity {

    override fun equals(other: Any?): Boolean {
        val obj = other as? CurrentWeatherData ?: return false
        return this.temp == obj.temp
                && this.cityId == obj.cityId
//                && this.icon == obj.icon
    }

    companion object {
        fun createFromRemoteEntity(cityId: Int, remoteEntity: Current) = with(remoteEntity) {
            CurrentWeatherData(
                cityId,
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
                weather[0].icon
            )
        }
    }
}

@Entity(tableName = "hourly_weather_data")
data class HourlyWeatherData(
    @PrimaryKey
    val cityId: Int,
    val hourlyList: List<Hourly>
) : WeatherEntity {
    companion object {
        fun createFromRemoteEntity(
            cityId: Int,
            remoteEntity: List<com.data.remote.entity.Hourly>
        ): HourlyWeatherData {
            val hourlyList = mutableListOf<Hourly>()
            remoteEntity.forEach {
                hourlyList.add(
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
                        it.weather[0].icon,
                        it.wind_deg,
                        it.wind_speed
                    )
                )
            }
            return HourlyWeatherData(cityId, hourlyList)
        }
    }
}

data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val dt: String,
    var feels_like: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    var temp: Double,
    val uvi: Double,
    val visibility: Int,
    val description: String,
    val icon: String,
    val wind_deg: Int,
    var wind_speed: Double
) {
    override fun toString() =
        "${component1()}|${component2()}|${component3()}|${component4()}|${component5()}|" +
                "${component6()}|${component7()}|${component8()}|${component9()}|${component10()}|" +
                "${component11()}|${component12()}|${component13()}|${component14()}|${component15()}"
}

@Entity(tableName = "daily_weather_data")
data class DailyWeatherData(
    @PrimaryKey
    val cityId: Int,
    val dailyList: List<Daily>
) : WeatherEntity {
    companion object {
        fun createFromRemoteEntity(
            cityId: Int,
            remoteEntity: List<com.data.remote.entity.Daily>
        ): DailyWeatherData {
            val dailyList = mutableListOf<Daily>()
            remoteEntity.forEach {
                dailyList.add(
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
                        it.weather[0].icon,
                        it.wind_deg,
                        it.wind_speed
                    )
                )
            }
            return DailyWeatherData(cityId, dailyList)
        }
    }

}

data class Daily(
    val clouds: Int,
    val dew_point: Double,
    val dt: String,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val snow: Double,
    val sunrise: String,
    val sunset: String,
    var tempDay: Double,
    var tempEve: Double,
    var tempMax: Double,
    var tempMin: Double,
    var tempMorn: Double,
    var tempNight: Double,
    val uvi: Double,
    val description: String,
    val icon: String,
    val wind_deg: Int,
    var wind_speed: Double
) {
    override fun toString() =
        "${component1()}|${component2()}|${component3()}|${component4()}|${component5()}|" +
                "${component6()}|${component7()}|${component8()}|${component9()}|${component10()}|" +
                "${component11()}|${component12()}|${component13()}|${component14()}|${component15()}" +
                "|${component16()}|${component17()}|${component18()}|${component19()}|${component20()}|${component21()}"
}

interface WeatherEntity

//для localCitiesList
data class CityCurrentWeatherRelation(
    @Embedded val city: City,
    @Relation(
        parentColumn = "cityId",
        entityColumn = "cityId"
    )
    val currentWeatherData: CurrentWeatherData?
)

////для settings reconfig
//data class CityAndFullWeather(
//    @Embedded val city: City,
//    @Relation(
//        parentColumn = "cityId",
//        entityColumn = "cityId"
//    )
//    val weathers: List<WeatherEntity>?
//
//)
//
