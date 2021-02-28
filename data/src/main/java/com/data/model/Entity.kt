package com.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.data.common.SEPARATOR
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

fun City.toRegex() = component1().toString() + SEPARATOR + component2() +
        SEPARATOR + component3() + SEPARATOR + component4() + SEPARATOR + component5() +
        SEPARATOR + component6() + SEPARATOR + component7() + SEPARATOR + component8() +
        SEPARATOR + component9() + SEPARATOR + component10()

@Entity(tableName = "weather_data")
data class WeatherData(
    @PrimaryKey
    val cityId: Int,
    @Embedded
    val currentWeatherData: CurrentWeatherData,
    val hourlyList: List<Hourly>,
    val dailyList: List<Daily>

)

data class CurrentWeatherData(
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
    val icon: Int,
) {
    override fun equals(other: Any?): Boolean {
        val obj = other as? CurrentWeatherData ?: return false
        return this.temp == obj.temp
//                && this.cityId == obj.cityId
//                && this.icon == obj.icon
    }
}

data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val dt: String,
    var feels_like: Double,
    val humidity: Int,
    val pop: Int,
    val pressure: Int,
    val rain: Double,
    var temp: Double,
    val uvi: Double,
    val visibility: Int,
    val description: String,
    val icon: Int,
    val wind_deg: Int,
    var wind_speed: Double
) {
    override fun toString() =
        "${component1()}$SEPARATOR${component2()}$SEPARATOR${component3()}$SEPARATOR${component4()}$SEPARATOR${component5()}$SEPARATOR" +
                "${component6()}$SEPARATOR${component7()}$SEPARATOR${component8()}$SEPARATOR${component9()}$SEPARATOR${component10()}$SEPARATOR" +
                "${component11()}$SEPARATOR${component12()}$SEPARATOR${component13()}$SEPARATOR${component14()}$SEPARATOR${component15()}"
}


data class Daily(
    val clouds: Int,
    val dew_point: Double,
    val dt: String,
    val humidity: Int,
    val pop: Int,
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
    val icon: Int,
    val wind_deg: Int,
    var wind_speed: Double
) {
    override fun toString() =
        "${component1()}$SEPARATOR${component2()}$SEPARATOR${component3()}$SEPARATOR${component4()}$SEPARATOR${component5()}$SEPARATOR" +
                "${component6()}$SEPARATOR${component7()}$SEPARATOR${component8()}$SEPARATOR${component9()}$SEPARATOR${component10()}$SEPARATOR" +
                "${component11()}$SEPARATOR${component12()}$SEPARATOR${component13()}$SEPARATOR${component14()}$SEPARATOR${component15()}" +
                "$SEPARATOR${component16()}$SEPARATOR${component17()}$SEPARATOR${component18()}$SEPARATOR${component19()}$SEPARATOR${component20()}$SEPARATOR${component21()}"
}


data class CityWeatherRelation(
    @Embedded val city: City,
    @Relation(
        parentColumn = "cityId",
        entityColumn = "cityId"
    )
    val weatherData: WeatherData?
)

//data class CityWithCurrentWeather(
//    @Embedded val city: City,
//    @Relation(
//        parentColumn = "cityId",
//        entityColumn = "cityId"
//    )
//    val currentWeatherData: CurrentWeatherData
//)

//
//class DepartmentWithEmployees {
//    @Embedded
//    var department: Department? = null
//
//    @Relation(parentColumn = "id", entityColumn = "department_id")
//    var employees: List<Employee>? = null
//}