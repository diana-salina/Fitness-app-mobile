package ru.nsu.salina.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.nsu.salina.data.local.entity.ActivePlanEntity

@Dao
interface ActivePlanDao {
    @Query("SELECT * FROM active_plan WHERE id = 1 LIMIT 1")
    fun getActivePlanFlow(): Flow<ActivePlanEntity?>

    @Query("SELECT * FROM active_plan WHERE id = 1 LIMIT 1")
    suspend fun getActivePlanOnce(): ActivePlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(plan: ActivePlanEntity)

    @Query("DELETE FROM active_plan WHERE id = 1")
    suspend fun deleteActivePlan()

    @Query("UPDATE active_plan SET currentDay = :day WHERE id = 1")
    suspend fun updateCurrentDay(day: Int)

    @Query("UPDATE active_plan SET isCompleted = 1 WHERE id = 1")
    suspend fun markCompleted()

    @Query("UPDATE active_plan SET restDayShownDate = :date WHERE id = 1")
    suspend fun updateRestDayShownDate(date: String)

    @Query("UPDATE active_plan SET restDayShownDate = NULL WHERE id = 1")
    suspend fun clearRestDayShownDate()

    @Query("UPDATE active_plan SET currentDayDate = :date WHERE id = 1")
    suspend fun updateCurrentDayDate(date: String)
}
