package ru.nsu.salina.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_workouts")
data class CompletedWorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val dayNumber: Int
)
