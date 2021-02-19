package com.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from cities")
    fun getFlowCityList(): Flow<List<City>>

    @Transaction
    @Query("select * from cities")
    fun getFlowCityCurrentWeatherRelationList(): Flow<List<CityCurrentWeatherRelation>>

    @Query("select * from cities")
    suspend fun getCityList(): List<City>

    @Insert
    suspend fun insertCity(city: City): Long

    @Transaction
    suspend fun insertWeathe(
        current: List<CurrentWeatherData>,
        hourly: List<HourlyWeatherData>,
        daily: List<DailyWeatherData>
    ) {
        current.forEach { insertCurrentWeather(it) }
        hourly.forEach { insertHourlyWeather(it) }
        daily.forEach { insertDailyWeather(it) }
    }

    @Transaction
    suspend fun insertWeather(vararg weatherData: Triple<CurrentWeatherData, HourlyWeatherData, DailyWeatherData>) {
        weatherData.forEach {
            insertCurrentWeather(it.first)
            insertHourlyWeather(it.second)
            insertDailyWeather(it.third)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(first: CurrentWeatherData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeather(second: HourlyWeatherData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWeather(third: DailyWeatherData)

    @Delete
    suspend fun deleteCity(city: City)

    @Transaction
    suspend fun deleteWeatherData(vararg cityId: Int) {
        cityId.forEach {
            deleteCurrentWeatherData(it)
            deleteHourlyWeatherData(it)
            deleteDailyWeatherData(it)
        }
    }

    @Query("delete from current_weather_data where cityId=:cityId")
    suspend fun deleteCurrentWeatherData(vararg cityId: Int)

    @Query("delete from hourly_weather_data where cityId=:cityId")
    suspend fun deleteHourlyWeatherData(vararg cityId: Int)

    @Query("delete from daily_weather_data where cityId=:cityId")
    suspend fun deleteDailyWeatherData(vararg cityId: Int)

    /**
     * вызывается только от usecase: ReorderLocalCities
     * поэтому предусмотрена возможность REPLACE
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun reorderLocalCities(vararg city: City): List<Long>

    @Query("select * from current_weather_data")
    suspend fun getCurrent(): List<CurrentWeatherData>

    @Query("select * from hourly_weather_data")
    suspend fun getHourly(): List<HourlyWeatherData>

    @Query("select * from daily_weather_data")
    suspend fun getDaily(): List<DailyWeatherData>


}
