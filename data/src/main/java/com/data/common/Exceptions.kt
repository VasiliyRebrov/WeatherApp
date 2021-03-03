package com.data.common



class NoNetworkException(msg: String = NO_NETWORK_MSG) : Exception(msg)
class NetworkProviderDisabledException(msg: String = NETWORK_PROVIDER_DISABLED_MSG) : Exception(msg)
class CitiesNotFoundException(msg: String = CITIES_NOT_FOUND_MSG) : Exception(msg)
class CityAlreadyExistException(msg: String = CITY_ALREADY_EXIST_MSG) : Exception(msg)
class InvalidArgsException(msg: String = INVALID_ARGS_MSG) : Exception(msg)

