package ru.nsu.salina.domain.model

data class Contraindication(val id: Int, val name: String)

data class Exercise(
    val name: String,
    val technique: String,
    val repetitions: Int?,
    val duration: Int?
)

data class DayPlan(
    val day: Int,
    val focus: String,
    val exercises: List<Exercise>
)

data class ActivePlan(
    val id: Long,
    val name: String,
    val currentDay: Int,
    val isCompleted: Boolean,
    val restDayShownDate: String?,
    val currentDayDate: String?
)

data class CompletedWorkout(
    val id: Long,
    val date: String,
    val dayNumber: Int
)

sealed class PlanGenerationResult {
    object Success : PlanGenerationResult()
    data class Failure(val message: String) : PlanGenerationResult()
}
