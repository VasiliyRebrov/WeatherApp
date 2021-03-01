package com.weather.common.entities

import android.content.Context
import android.preference.PreferenceManager
import com.data.common.SingletonHolder
import com.weather.R


/**
 * Класс Config инкапсулирует получение значений настроек
 * В данный момент имеется только один аттрибут настроек:
 * - единица измерения
 * */

class Config private constructor(private val ctx: Context) {
    val unitMeasurePref: String
        get() = getPrefValue(ctx.getString(R.string.preference_unit_measure_key)) ?: "Metric"


    companion object : SingletonHolder<Config, Context>({ ctx -> Config(ctx) })

    private fun getPrefValue(key: String): String? {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx)
        return sharedPref.getString(key, null)
    }
}

fun Context.getWindUnits() =
    if (Config.getInstance(this).unitMeasurePref == "Metric") "м/сек" else "ми/ч"

fun Context.getTempUnits() =
    if (Config.getInstance(this).unitMeasurePref == "Metric") "C" else "F"




