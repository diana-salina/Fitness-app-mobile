package ru.nsu.salina.ui.navigation

object NavRoutes {
    const val WORKOUTS = "workouts"
    const val WORKOUT_DETAIL = "workouts/detail"
    const val WORKOUT_EXECUTION = "workouts/execution"
    const val CONTRAINDICATIONS = "workouts/contraindications"
    const val PLAN_LOADING = "workouts/loading/{contraindicationIds}"
    const val COMPLETION = "workouts/completion/{isPlanComplete}"
    const val CALENDAR = "calendar"
    const val PROFILE = "profile"

    fun planLoading(ids: List<Int>): String =
        "workouts/loading/${if (ids.isEmpty()) "none" else ids.joinToString(",")}"

    fun completion(isPlanComplete: Boolean): String = "workouts/completion/$isPlanComplete"
}

val BOTTOM_BAR_ROUTES = listOf(NavRoutes.WORKOUTS, NavRoutes.CALENDAR, NavRoutes.PROFILE)
