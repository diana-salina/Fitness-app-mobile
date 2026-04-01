package ru.nsu.salina.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import ru.nsu.salina.data.local.entity.DayPlanEntity
import ru.nsu.salina.data.local.entity.ExerciseEntity

data class DayPlanWithExercises(
    @Embedded val dayPlan: DayPlanEntity,
    @Relation(parentColumn = "id", entityColumn = "dayPlanId")
    val exercises: List<ExerciseEntity>
)

@Dao
interface DayPlanDao {
    @Insert
    suspend fun insert(dayPlan: DayPlanEntity): Long

    @Transaction
    @Query("SELECT * FROM day_plans WHERE planId = :planId AND day = :day LIMIT 1")
    suspend fun getDayPlanWithExercises(planId: Long, day: Int): DayPlanWithExercises?
}
