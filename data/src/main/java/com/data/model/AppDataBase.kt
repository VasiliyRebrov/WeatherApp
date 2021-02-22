package com.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.data.common.Converters
import com.data.common.SingletonHolder

@Database(
    entities = [City::class, CurrentWeatherData::class, HourlyWeatherData::class, DailyWeatherData::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object : SingletonHolder<AppDataBase, Context>({ ctx ->
        Room.databaseBuilder(ctx.applicationContext, AppDataBase::class.java, "weather.db").build()
    })
}