package com.weather.view

data class Resource<T>(
    val data: T?,
    val exception: Exception?
)

enum class ProgressStatus {
    LOADING,
    COMPLETED
}

enum class ItemTouchAction {
    DEFAULT,
    MOVE,
    SWIPE,
}

enum class DialogAlertType {
    ALLOW_LOCATION_PERMISSION,
    ENABLE_LOCATION
}