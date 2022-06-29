package com.example.android.composedogs_udemyjetpackcourse.data.app_settings

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val StoredTime: Long = 5L,
    val cacheDuration: Int = 10
)