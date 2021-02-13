package com.data.repo

import android.content.Context
import androidx.lifecycle.asLiveData
import com.data.common.Result
import com.data.model.City
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.lang.Exception
import java.lang.StringBuilder

class MainRepo(ctx: Context) : BaseRepo(ctx) {
    val localCities
        get() = dao.getLiveCityList()

    fun refreshWeatherData(newCities: List<City>, oldCities: List<City>) = flow {
        try {
            emit(Result.Loading)
            val resultString = StringBuilder()
            if (newCities.isNotEmpty()) {
                val weatherData = loadWeatherData(newCities)
                dao.insertWeather(*weatherData.toTypedArray())
                resultString.append("added: ${newCities.size} elements\n")
            }
            if (oldCities.isNotEmpty()) {
                dao.deleteWeatherData(*oldCities.map { it.cityId }.toIntArray())
                resultString.append("deleted: ${oldCities.size} elements\n")
            }
            emit(Result.Success(resultString.toString()))
        } catch (exc: Exception) {
            emit(Result.Error(exc))
        }
    }
//    val cities = dao.getLiveCityList().map { it.sortedBy { city -> city.serialNumber } }
    //сейчас здесь просто преобразование
    //но что если учесть случай, когда у нас простая пересортировка списка, и не следует слать в ливдату новые данные
    //попытаться текущий flow сначала пересортировать, но не по serialnumber, а по id, а потом учесть distinct.
    //получится так, что пересортированный список, тк. для нас он не менялся, не будет поступать дальше
    //это пока предположение. мб и нет смысла. ничего не стоит, просто перепроверить в методе
}
