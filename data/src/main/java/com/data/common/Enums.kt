package com.data.common

enum class Units(val text: String) {
    METRIC("metric"), IMPERIAL("imperial")
}

enum class ResponseStatusEnum(val text: String) {
    REMOTE_LIST(REMOTE_LIST_TITLE), LOCAL_LIST(LOCAL_LIST_TITLE), EMPTY(EMPTY_TITLE)

}

enum class AddCityUseCases {
   SETUPLOCATION, FINDCITY, ADDCITY
}