package ru.nsu.salina.ui.screens.workouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.nsu.salina.data.repository.TrainingRepository
import ru.nsu.salina.ui.components.LoadingScreen
import ru.nsu.salina.ui.navigation.NavRoutes

@Composable
fun PlanLoadingScreen(
    contraindicationIds: List<Int>,
    repository: TrainingRepository,
    navController: NavController
) {
    val viewModel: PlanLoadingViewModel = viewModel(
        factory = PlanLoadingViewModel.factory(repository, contraindicationIds)
    )
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is PlanLoadingState.Done) {
            val done = state as PlanLoadingState.Done
            try {
                navController.getBackStackEntry(NavRoutes.WORKOUTS)
                    .savedStateHandle["planResult"] = done.message
            } catch (_: Exception) { }
            navController.popBackStack(NavRoutes.WORKOUTS, inclusive = false)
        }
    }

    LoadingScreen(message = "Формируем план тренировок...")
}
