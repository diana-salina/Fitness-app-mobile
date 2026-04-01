package ru.nsu.salina.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrainingPlanErrorDto(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String
)
