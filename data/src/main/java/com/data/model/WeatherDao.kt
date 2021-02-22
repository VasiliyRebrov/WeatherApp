package com.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from cities")
    fun getFlowCities(): Flow<List<City>>

    @Transaction
    @Query("select * from cities")
    fun getFlowCityCurrentWeatherRelationList(): Flow<List<CityCurrentWeatherRelation>>

    //временный метод. далее будем брать всю погоду
    @Query("select * from current_weather_data where cityId=:cityId")
    fun getCurrentWeather(cityId: Int): Flow<CurrentWeatherData>

    @Query("select * from cities")
    suspend fun getCities(): List<City>

    @Query("select * from current_weather_data")
    suspend fun getCurrentWeather(): List<CurrentWeatherData>

    @Query("select * from hourly_weather_data")
    suspend fun getHourlyWeather(): List<HourlyWeatherData>

    @Query("select * from daily_weather_data")
    suspend fun getDailyWeather(): List<DailyWeatherData>


    /**
     * [insertCity]
     * Вызывается:
     * Для добавления города.
     * Кейсы:
     * 1) При добавлении нового города.
     * Возвращает: [Long] id удаленного города
     * */
    @Insert
    suspend fun insertCity(city: City): Long

    /**
     * [deleteCity]
     * Вызывается: Для удаления города.
     * Кейсы:
     * 1) При удаления города.
     * Возвращает: [Int] количество удаленных городов. (сейчас только один)
     * */
    @Delete
    suspend fun deleteCity(city: City): Int

    /**
     * [updateCities]
     * Вызывается: Для обновления существующих городов
     * Кейсы:
     * 1) При пересортировке городов. Меняется их поле [City.position] и происходит вставка с заменой.
     * Возвращает: [Int] количество обновленных записей
     * */
    @Update
    suspend fun updateCities(vararg city: City): Int

    /**
     * [insertTransformedData]
     * Вызывается:
     * Для переконфигурации значений погодных данных.
     * Кейсы:
     * При изменении единий измерения в настройках.
     */
    @Transaction
    suspend fun insertTransformedData(
        current: List<CurrentWeatherData>,
        hourly: List<HourlyWeatherData>,
        daily: List<DailyWeatherData>
    ) {
        current.forEach { insertCurrentWeather(it) }
        hourly.forEach { insertHourlyWeather(it) }
        daily.forEach { insertDailyWeather(it) }
    }

    /**
     * [insertActualData]
     * Вызывается:
     * Для добавлении погоды.
     * Кейсы:
     * При добавления погоды нового города.
     * При обновлении существующей погоды.
     * */
    @Transaction
    suspend fun insertActualData(vararg weatherData: Triple<CurrentWeatherData, HourlyWeatherData, DailyWeatherData>) {
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


    /**
     * [deleteWeatherData]
     * Вызывается:
     * Для удаления погоды.
     * Кейсы:
     * Удаление погоды города/городов, после удаление самих городов.
     * */
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
}
