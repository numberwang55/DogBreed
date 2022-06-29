package com.example.android.composedogs_udemyjetpackcourse.data.remote.dto

import com.example.android.composedogs_udemyjetpackcourse.domain.model.Weight
import com.google.gson.annotations.SerializedName

data class WeightDto(
    @SerializedName("imperial")
    val imperialWeight: String,
    @SerializedName("metric")
    val metricWeight: String
) {
    fun toWeight(): Weight {
        return Weight(
            imperialWeight,
            metricWeight
        )
    }
}