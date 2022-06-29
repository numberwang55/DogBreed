package com.example.android.composedogs_udemyjetpackcourse.data.remote.dto

import com.example.android.composedogs_udemyjetpackcourse.domain.model.Height
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class HeightDto(
    @SerializedName("imperial")
    val imperialHeight: String,
    @SerializedName("metric")
    val metricHeight: String
){
    fun toHeight(): Height{
        return Height(
            imperialHeight,
            metricHeight
        )
    }
}