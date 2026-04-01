package ru.nsu.salina.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import ru.nsu.salina.data.local.AppDatabase
import ru.nsu.salina.data.local.toDomain
import ru.nsu.salina.data.local.toEntity
import ru.nsu.salina.data.local.entity.ActivePlanEntity
import ru.nsu.salina.data.local.entity.CompletedWorkoutEntity
import ru.nsu.salina.data.remote.ApiService
import ru.nsu.salina.data.remote.dto.TrainingPlanErrorDto
import ru.nsu.salina.domain.model.ActivePlan
import ru.nsu.salina.domain.model.CompletedWorkout
import ru.nsu.salina.domain.model.Contraindication
import ru.nsu.salina.domain.model.DayPlan
import ru.nsu.salina.domain.model.PlanGenerationResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TrainingRepositoryImpl(
    private val db: AppDatabase,
    private val apiService: ApiService
) : TrainingRepository {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    private fun today(): String = LocalDate.now().format(formatter)

    override fun getActivePlanFlow(): Flow<ActivePlan?> =
        db.activePlanDao().getActivePlanFlow().map { it?.toDomain() }

    override fun getCompletedDatesFlow(): Flow<List<String>> =
        db.completedWorkoutDao().getAllDatesFlow()

    override fun getCompletedWorkoutsFlow(): Flow<List<CompletedWorkout>> =
        db.completedWorkoutDao().getAllWorkoutsFlow().map { list -> list.map { it.toDomain() } }

    override suspend fun getContraindications(): Result<List<Contraindication>> =
        runCatching { apiService.getContraindications().map { it.toDomain() } }

    override suspend fun generateAndSavePlan(contraindicationIds: List<Int>): PlanGenerationResult {
        return try {
            val dayPlans = apiService.getTrainingPlan(contraindicationIds)
            // Clear old plan data (cascade deletes day_plans and exercises)
            db.activePlanDao().deleteActivePlan()
            // Create new plan
            db.activePlanDao().insertOrUpdate(
                ActivePlanEntity(
                    id = 1L,
                    name = PLAN_NAME,
                    currentDay = 1,
                    isCompleted = false,
                    restDayShownDate = null,
                    currentDayDate = today()
                )
            )
            // Insert all days and their exercises
            for (dto in dayPlans) {
                val dayPlanId = db.dayPlanDao().insert(dto.toEntity(planId = 1L))
                if (dto.exercises.isNotEmpty()) {
                    db.exerciseDao().insertAll(dto.exercises.map { it.toEntity(dayPlanId) })
                }
            }
            PlanGenerationResult.Success
        } catch (e: HttpException) {
            val message = if (e.code() == 422) {
                try {
                    Gson().fromJson(e.response()?.errorBody()?.string(), TrainingPlanErrorDto::class.java).message
                } catch (_: Exception) {
                    "Не удалось сформировать план"
                }
            } else {
                "Ошибка сети (${e.code()})"
            }
            PlanGenerationResult.Failure(message)
        } catch (e: Exception) {
            PlanGenerationResult.Failure(e.message ?: "Неизвестная ошибка")
        }
    }

    override suspend fun getCurrentDayPlan(): DayPlan? {
        val plan = db.activePlanDao().getActivePlanOnce() ?: return null
        return db.dayPlanDao().getDayPlanWithExercises(1L, plan.currentDay)?.toDomain()
    }

    override suspend fun completeCurrentDay() {
        val plan = db.activePlanDao().getActivePlanOnce() ?: return
        db.completedWorkoutDao().insert(
            CompletedWorkoutEntity(date = today(), dayNumber = plan.currentDay)
        )
    }

    override suspend fun finishActivePlan() {
        db.activePlanDao().markCompleted()
    }

    override suspend fun checkAndAdvanceDay() {
        val plan = db.activePlanDao().getActivePlanOnce() ?: return
        if (plan.isCompleted) return

        val today = today()

        // Check if a regular workout day was completed on a previous date
        val lastCompletion = db.completedWorkoutDao().getLastCompletionForDay(plan.currentDay)
        if (lastCompletion != null && lastCompletion.date < today) {
            advanceDay(plan)
            return
        }

        // Check if rest day auto-advance is needed
        val restDate = plan.restDayShownDate
        if (restDate != null && restDate < today) {
            db.activePlanDao().clearRestDayShownDate()
            advanceDay(plan)
            return
        }

    }

    override suspend fun markRestDayShown() {
        db.activePlanDao().updateRestDayShownDate(today())
    }

    private suspend fun advanceDay(plan: ActivePlanEntity) {
        val nextDay = plan.currentDay + 1
        if (nextDay > 14) {
            db.activePlanDao().markCompleted()
        } else {
            db.activePlanDao().updateCurrentDay(nextDay)
            db.activePlanDao().updateCurrentDayDate(today())
        }
    }

    companion object {
        const val PLAN_NAME = "14-дневный курс"
    }
}
