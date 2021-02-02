package com.data.remote.api

import android.location.Location

interface Params
data class LoadWeatherParams(
    val lat: String,
    val lon: String,
    val apiKey: String = "ac4d0e2053d938b703a7a3c24806fb27",
    val units: String,
    val lang: String = "ru",
    val exclude: String = "minutely"
):Params

data class LoadCitiesParams private constructor(
    val namePrefix: String = "",
    val location: String = "",
    val radius: String = "",
    val distanceUnit: String = "",
    val sort: String = "",
    val languageCode: String = "en",    //изменил на англ для тестирования
    val limit: String = "10",
    val types: String = "CITY",
    val hateoasMode: String = "false",
    val api: String = "wft-geo-db.p.rapidapi.com",
    val apiKey: String = "640992a5cfmsha1ec8c3f9f11b41p140c4djsn1c2b8bd77454"
) :Params{
    companion object {
        fun createParamsByName(namePrefix: String) = LoadCitiesParams(
            namePrefix = namePrefix,
            sort = "-population"
        )

        fun createParamsByLocation(location: Location) = LoadCitiesParams(
            location = location.formatLocation(),
            radius = "10",
            distanceUnit = "KM"
        )
    }
}

fun Location.formatLocation(): String {
    fun Double.getPrefix() = if (this > 0) "+" else "-"
    return latitude.getPrefix() + latitude + longitude.getPrefix() + longitude
}

