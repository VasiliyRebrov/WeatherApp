package com.data.remote.api

import android.location.Location
import com.data.model.City

sealed class RequestParams {
    data class WeatherRequestParams private constructor(
        val lat: String,
        val lon: String,
        val apiKey: String = "ac4d0e2053d938b703a7a3c24806fb27",
        val units: String,
        val lang: String,
        val exclude: String = "minutely"
    ) : RequestParams() {
        companion object {
            fun createParams(city: City, unitMeasurePref: String, lang: String) =
                WeatherRequestParams(
                    lat = city.latitude,
                    lon = city.longitude,
                    units = unitMeasurePref,
                    lang = lang
                )
        }
    }

    data class GeoRequestParams private constructor(
        val namePrefix: String = "",
        val location: String = "",
        val radius: String = "",
        val distanceUnit: String = "",
        val sort: String = "",
        val languageCode: String,
        val limit: String = "10",
        val types: String = "CITY",
        val hateoasMode: String = "false",
        val api: String = "wft-geo-db.p.rapidapi.com",
        val apiKey: String = "640992a5cfmsha1ec8c3f9f11b41p140c4djsn1c2b8bd77454"
    ) : RequestParams() {
        companion object {
            fun createParamsByName(namePrefix: String, languageCode: String) = GeoRequestParams(
                namePrefix = namePrefix,
                sort = "-population",
                languageCode = languageCode
            )

            fun createParamsByLoc(location: Location, languageCode: String) = GeoRequestParams(
                location = location.formatLocation(),
                radius = "10",
                distanceUnit = "KM",
                languageCode = languageCode
            )
        }
    }
}

fun Location.formatLocation(): String {
    fun Double.withPrefix() = (if (this > 0) "+" else "-") + this
    return latitude.withPrefix() + longitude.withPrefix()
}