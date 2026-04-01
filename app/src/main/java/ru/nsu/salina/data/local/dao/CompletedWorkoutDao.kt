package ru.nsu.salina.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.nsu.salina.data.local.entity.CompletedWorkoutEntity

@Dao
interface CompletedWorkoutDao {
    @Insert
    suspend fun insert(workout: CompletedWorkoutEntity)

    @Query("SELECT date FROM completed_workouts")
    fun getAllDatesFlow(): Flow<List<String>>

    @Query("SELECT * FROM completed_workouts WHERE dayNumber = :dayNumber ORDER BY date DESC LIMIT 1")
    suspend fun getLastCompletionForDay(dayNumber: Int): CompletedWorkoutEntity?

    @Query("SELECT * FROM completed_workouts")
    fun getAllWorkoutsFlow(): Flow<List<CompletedWorkoutEntity>>
}
