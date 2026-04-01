package ru.nsu.salina.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "active_plan")
data class ActivePlanEntity(
    @PrimaryKey val id: Long = 1L,
    val name: String,
    val currentDay: Int,
    val isCompleted: Boolean,
    val restDayShownDate: String?,
    val currentDayDate: String?
)
