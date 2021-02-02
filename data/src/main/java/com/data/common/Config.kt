package com.data.common

import android.content.Context
import android.preference.PreferenceManager

class Config private constructor(private val context: Context) {
    val unitsPref: String
        get() = getPrefValue("temp_key")

    companion object : SingletonHolder<Config, Context>({ Config(it) })

    private fun getPrefValue(key: String): String {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref?.getString(key, "Metric") ?:
        "Metric"
    }
}

