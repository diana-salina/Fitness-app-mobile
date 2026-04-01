package ru.nsu.salina.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.nsu.salina.data.local.entity.ExerciseEntity

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insertAll(exercises: List<ExerciseEntity>)
}
