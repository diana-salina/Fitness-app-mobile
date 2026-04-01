package ru.nsu.salina.data.repository

import kotlinx.coroutines.flow.Flow
import ru.nsu.salina.domain.model.ActivePlan
import ru.nsu.salina.domain.model.CompletedWorkout
import ru.nsu.salina.domain.model.Contraindication
import ru.nsu.salina.domain.model.DayPlan
import ru.nsu.salina.domain.model.PlanGenerationResult

interface TrainingRepository {
    fun getActivePlanFlow(): Flow<ActivePlan?>
    fun getCompletedDatesFlow(): Flow<List<String>>
    fun getCompletedWorkoutsFlow(): Flow<List<CompletedWorkout>>
    suspend fun getContraindications(): Result<List<Contraindication>>
    suspend fun generateAndSavePlan(contraindicationIds: List<Int>): PlanGenerationResult
    suspend fun getCurrentDayPlan(): DayPlan?
    suspend fun completeCurrentDay()
    suspend fun finishActivePlan()
    suspend fun checkAndAdvanceDay()
    suspend fun markRestDayShown()
}
