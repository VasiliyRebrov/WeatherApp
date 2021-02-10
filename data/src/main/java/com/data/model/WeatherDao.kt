package com.data.model

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from cities")
    fun getLiveCityList(): Flow<List<City>>

    @Query("select * from cities")
    suspend fun getCityList(): List<City>

    @Insert
    suspend fun insertCity(city: City):Long


//    //    @Transaction
////    @Query("SELECT * FROM cities")
////    fun getFullWeather(): List<CityAndFullWeather>
//    @Query("select * from current_weather_data")
//    fun getCurrent(): List<CurrentWeatherData>
//
//    @Query("select * from hourly_weather_data")
//    fun getHourly(): List<HourlyWeatherData>
//
//    @Query("select * from daily_weather_data")
//    fun getDaily(): List<DailyWeatherData>
//
//    @Transaction
//    @Query("select * from cities")
//    fun getItemsData(): LiveData<List<CityAndCurrentWeather>>
//
//    @Insert
//    fun insertCity(city: City): Long?
//
//    @Query("select * from current_weather_data")
//    fun getWeathers(): List<CurrentWeatherData>
//
//    @Transaction
//    fun insertWeathe(
//        current: List<CurrentWeatherData>,
//        hourly: List<HourlyWeatherData>,
//        daily: List<DailyWeatherData>
//    ) {
//        current.forEach { insertCurrentWeather(it) }
//        hourly.forEach { insertHourlyWeather(it) }
//        daily.forEach { insertDailyWeather(it) }
//    }
//
//    @Transaction
//    fun insertWeather(vararg weatherData: Triple<CurrentWeatherData, HourlyWeatherData, DailyWeatherData>) {
//        weatherData.forEach {
//            insertCurrentWeather(it.first)
//            insertHourlyWeather(it.second)
//            insertDailyWeather(it.third)
//        }
//    }
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertCurrentWeather(first: CurrentWeatherData)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertHourlyWeather(second: HourlyWeatherData)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertDailyWeather(third: DailyWeatherData)
//
//
//    @Transaction
//    fun deleteCityAndSort(city: City, sortedList: MutableList<City>) {
//        deleteCity(city)
//        deleteCurrentWeatherData(city.cityId)
//        deleteHourlyWeatherData(city.cityId)
//        deleteDailyWeatherData(city.cityId)
//        insert(*sortedList.toTypedArray())
//    }
//
//    @Delete
//    fun deleteCity(city: City)
//
//    @Query("delete from current_weather_data where cityId=:cityId")
//    fun deleteCurrentWeatherData(vararg cityId: Int)
//
//    @Query("delete from hourly_weather_data where cityId=:cityId")
//    fun deleteHourlyWeatherData(vararg cityId: Int)
//
//    @Query("delete from daily_weather_data where cityId=:cityId")
//    fun deleteDailyWeatherData(vararg cityId: Int)
//
//
//    @Query("select * from current_weather_data where cityId=:cityId")
//    fun getCurrentWeather(cityId: Int): LiveData<CurrentWeatherData>
//
//
//    @Query("select * from cities")
//    fun getLiveCityList(): LiveData<List<City>>
//
//    @Query("select * from cities")
//    fun getCityList(): List<City>
//
//    //возможность замены предусмотрена, тк. сюда приходят города после пересортировки
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(vararg city: City): List<Long>
}
