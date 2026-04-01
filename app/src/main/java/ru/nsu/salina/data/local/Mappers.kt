package ru.nsu.salina.data.local

import ru.nsu.salina.data.local.dao.DayPlanWithExercises
import ru.nsu.salina.data.local.entity.ActivePlanEntity
import ru.nsu.salina.data.local.entity.CompletedWorkoutEntity
import ru.nsu.salina.data.local.entity.ExerciseEntity
import ru.nsu.salina.data.remote.dto.ContraindicationDto
import ru.nsu.salina.data.remote.dto.DayPlanDto
import ru.nsu.salina.data.remote.dto.ExerciseDto
import ru.nsu.salina.domain.model.ActivePlan
import ru.nsu.salina.domain.model.CompletedWorkout
import ru.nsu.salina.domain.model.Contraindication
import ru.nsu.salina.domain.model.DayPlan
import ru.nsu.salina.domain.model.Exercise

fun ActivePlanEntity.toDomain() = ActivePlan(
    id = id,
    name = name,
    currentDay = currentDay,
    isCompleted = isCompleted,
    restDayShownDate = restDayShownDate,
    currentDayDate = currentDayDate
)

fun DayPlanWithExercises.toDomain() = DayPlan(
    day = dayPlan.day,
    focus = dayPlan.focus,
    exercises = exercises.map { it.toDomain() }
)

fun ExerciseEntity.toDomain() = Exercise(
    name = name,
    technique = technique,
    repetitions = repetitions,
    duration = duration
)

fun CompletedWorkoutEntity.toDomain() = CompletedWorkout(
    id = id,
    date = date,
    dayNumber = dayNumber
)

fun ContraindicationDto.toDomain() = Contraindication(id = id, name = name)

fun DayPlanDto.toEntity(planId: Long) = ru.nsu.salina.data.local.entity.DayPlanEntity(
    planId = planId,
    day = day,
    focus = focus
)

fun ExerciseDto.toEntity(dayPlanId: Long) = ExerciseEntity(
    dayPlanId = dayPlanId,
    name = name,
    technique = technique,
    repetitions = repetitions,
    duration = duration
)
