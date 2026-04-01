package ru.nsu.salina.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = DayPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["dayPlanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("dayPlanId")]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayPlanId: Long,
    val name: String,
    val technique: String,
    val repetitions: Int?,
    val duration: Int?
)
