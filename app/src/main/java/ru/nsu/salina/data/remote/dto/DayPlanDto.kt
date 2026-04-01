package ru.nsu.salina.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DayPlanDto(
    @SerializedName("day") val day: Int,
    @SerializedName("focus") val focus: String,
    @SerializedName("exercises") val exercises: List<ExerciseDto>
)

data class ExerciseDto(
    @SerializedName("name") val name: String,
    @SerializedName("technique") val technique: String,
    @SerializedName("repetitions") val repetitions: Int?,
    @SerializedName("duration") val duration: Int?
)
