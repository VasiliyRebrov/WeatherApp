package com.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    /**
     * [getFlowCities]
     * Вызывается:
     * Для получения Flow списка типа городов
     * Кейсы:
     * 1) получение списка в Main
     * */
    @Query("select * from cities")
    fun getFlowCities(): Flow<List<City>>

    /**
     * [getFlowCityWeatherRelationList]
     * Вызывается:
     * Для получения Flow списка  типа связки город - соответствующие данные по погоде
     * Кейсы:
     * 1) получение списка в CitiesManager
     * */
    @Transaction
    @Query("select * from cities")
    fun getFlowCityWeatherRelationList(): Flow<List<CityWeatherRelation>>

    /**
     * [getFlowWeatherDataByCity]
     * Вызывается:
     * Для получения Flow погоды, соответствующей id города
     * Кейсы:
     * 1) получение погоды в CityItem
     * */
    @Query("select * from weather_data where cityId=:cityId")
    fun getFlowWeatherDataByCity(cityId: Int): Flow<WeatherData?>

    /**
     * [getWeatherData]
     * Вызывается:
     * Для получения списка типа погодных данных
     * Кейсы:
     * 1) получение списка в Settings для конвертирования погоды в другую систему измерения
     * */
    @Query("select * from weather_data")
    suspend fun getWeatherData(): List<WeatherData>

    /**
     * [getCities]
     * Вызывается:
     * Для получения списка городов
     * Кейсы:
     * 1) получение в AddCity, при добавлении города, для проверки наличия его в списке уже добавленных,
     * а также для присвоения ему [City.position] относительно размера списка
     * */
    @Query("select * from cities")
    suspend fun getCities(): List<City>

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
     * [insertActualWeatherData]
     * Вызывается: Для обновления существующих погодных данных
     * Кейсы:
     * 1) в Main, при добавлении\обновлении погоды
     * 2) в Settings, при внесении погоды, с конвертированным значением
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActualWeatherData(vararg data: WeatherData)


    @Query("delete from weather_data where cityId=:cityId")
    suspend fun deleteWeatherData(vararg cityId: Int)
}
