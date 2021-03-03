package com.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from cities")
    fun getFlowCities(): Flow<List<City>>

    @Transaction
    @Query("select * from cities")
    fun getFlowCityDataList(): Flow<List<CityData>>

    @Query("select * from weather_data where cityId=:cityId")
    fun getFlowDataById(cityId: Int): Flow<WeatherData?>

    @Query("select * from weather_data")
    suspend fun getData(): List<WeatherData>

    @Query("select * from cities")
    suspend fun getCities(): List<City>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(vararg data: WeatherData): List<Long>

    @Insert
    suspend fun insertCity(city: City): Long

    @Update
    suspend fun updateCities(vararg city: City): Int

    @Query("delete from weather_data where cityId=:cityId")
    suspend fun deleteDataById(vararg cityId: Int): Int

    @Delete
    suspend fun deleteCity(city: City): Int
}
