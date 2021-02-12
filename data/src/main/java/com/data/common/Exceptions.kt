package com.data.common

const val CITIES_NOT_FOUND_MSG = "cities not found"
const val NO_NETWORK_MSG = "no network"
const val CITY_ALREADY_EXIST_MSG = "city already exist"
const val INVALID_ARGS_MSG = "input something"
const val NETWORK_PROVIDER_DISABLED = "network provider disabled"

class CitiesNotFoundException(msg: String = CITIES_NOT_FOUND_MSG) : Exception(msg)
class NoNetworkException(msg: String = NO_NETWORK_MSG) : Exception(msg)
class CityAlreadyExistException(msg: String = CITY_ALREADY_EXIST_MSG) : Exception(msg)

class NetworkProviderDisabledException(msg: String = NETWORK_PROVIDER_DISABLED) : Exception(msg)

//предупреждать, что вводить нужно на данном языке
//потому что параметр rest api-запроса lang
//будет подставляться по языку системы
class InvalidArgsException(msg: String = INVALID_ARGS_MSG) : Exception(msg)

