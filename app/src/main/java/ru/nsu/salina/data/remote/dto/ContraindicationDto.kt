package ru.nsu.salina.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ContraindicationDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
