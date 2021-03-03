package com.data.common

import androidx.room.TypeConverter
import com.data.model.DailyWeather
import com.data.model.HourlyWeather

class Converters {
    @TypeConverter
    fun hourlyStringToList(regex: String) = regex.split(SUB_SEPARATOR).map { regexItem ->
        return@map with(regexItem.split(SEPARATOR)) {
            HourlyWeather(
                this[0].toInt(),
                this[1].toDouble(),
                this[2],
                this[3].toDouble(),
                this[4].toInt(),
                this[5].toInt(),
                this[6].toInt(),
                this[7].toDouble(),
                this[8].toDouble(),
                this[9].toDouble(),
                this[10].toInt(),
                this[11],
                this[12].toInt(),
                this[13].toInt(),
                this[14].toDouble()
            )
        }
    }

    @TypeConverter
    fun dailyStringToList(regex: String) = regex.split(SUB_SEPARATOR).map { regexItem ->
        return@map with(regexItem.split(SEPARATOR)) {
            DailyWeather(
                this[0].toInt(),
                this[1].toDouble(),
                this[2],
                this[3].toInt(),
                this[4].toInt(),
                this[5].toInt(),
                this[6].toDouble(),
                this[7].toDouble(),
                this[8],
                this[9],
                this[10].toDouble(),
                this[11].toDouble(),
                this[12].toDouble(),
                this[13].toDouble(),
                this[14].toDouble(),
                this[15].toDouble(),
                this[16].toDouble(),
                this[17],
                this[18].toInt(),
                this[19].toInt(),
                this[20].toDouble()
            )
        }
    }

    @TypeConverter
    fun hourlyListToString(list: List<HourlyWeather>) = createString(list)

    @TypeConverter
    fun dailyListToString(list: List<DailyWeather>) = createString(list)

    private fun <T> createString(list: List<T>) = list.joinToString(SUB_SEPARATOR) { it.toString() }
}

// поменять саб и сепаратор местами