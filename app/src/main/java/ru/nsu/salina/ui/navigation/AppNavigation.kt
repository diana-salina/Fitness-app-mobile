package ru.nsu.salina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import ru.nsu.salina.data.repository.TrainingRepository
import ru.nsu.salina.ui.components.AppBottomBar
import ru.nsu.salina.ui.screens.calendar.CalendarScreen
import ru.nsu.salina.ui.screens.calendar.CalendarViewModel
import ru.nsu.salina.ui.screens.profile.ProfileScreen
import ru.nsu.salina.ui.screens.workouts.CompletionScreen
import ru.nsu.salina.ui.screens.workouts.ContraindicationsScreen
import ru.nsu.salina.ui.screens.workouts.ContraindicationsViewModel
import ru.nsu.salina.ui.screens.workouts.ExecutionViewModel
import ru.nsu.salina.ui.screens.workouts.PlanLoadingScreen
import ru.nsu.salina.ui.screens.workouts.WorkoutExecutionScreen
import ru.nsu.salina.ui.screens.workouts.WorkoutsScreen
import ru.nsu.salina.ui.screens.workouts.WorkoutsViewModel

@Composable
fun AppNavigation(repository: TrainingRepository) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showDetailSheet by remember { mutableStateOf(false) }

    // WorkoutsViewModel is scoped at NavGraph level so it survives tab switches
    val workoutsViewModel: WorkoutsViewModel = viewModel(
        factory = WorkoutsViewModel.factory(repository)
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in BOTTOM_BAR_ROUTES) {
                AppBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(NavRoutes.WORKOUTS) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.WORKOUTS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.WORKOUTS) {
                WorkoutsScreen(
                    viewModel = workoutsViewModel,
                    navController = navController,
                    showDetailSheet = showDetailSheet,
                    onShowDetail = { showDetailSheet = true },
                    onDismissDetail = { showDetailSheet = false }
                )
            }

            composable(NavRoutes.WORKOUT_EXECUTION) {
                WorkoutExecutionScreen(
                    viewModel = viewModel(factory = ExecutionViewModel.factory(repository)),
                    navController = navController
                )
            }

            composable(NavRoutes.CONTRAINDICATIONS) {
                ContraindicationsScreen(
                    viewModel = viewModel(factory = ContraindicationsViewModel.factory(repository)),
                    navController = navController
                )
            }

            composable(
                route = NavRoutes.PLAN_LOADING,
                arguments = listOf(
                    navArgument("contraindicationIds") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val idsString = backStackEntry.arguments?.getString("contraindicationIds") ?: "none"
                val ids = if (idsString == "none" || idsString.isBlank()) {
                    emptyList()
                } else {
                    idsString.split(",").mapNotNull { it.trim().toIntOrNull() }
                }
                PlanLoadingScreen(
                    contraindicationIds = ids,
                    repository = repository,
                    navController = navController
                )
            }

            composable(
                route = NavRoutes.COMPLETION,
                arguments = listOf(
                    navArgument("isPlanComplete") { type = NavType.BoolType }
                )
            ) { backStackEntry ->
                val isPlanComplete = backStackEntry.arguments?.getBoolean("isPlanComplete") ?: false
                CompletionScreen(
                    isPlanComplete = isPlanComplete,
                    onDone = {
                        navController.navigate(NavRoutes.WORKOUTS) {
                            popUpTo(NavRoutes.WORKOUTS) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavRoutes.CALENDAR) {
                CalendarScreen(
                    viewModel = viewModel(factory = CalendarViewModel.factory(repository))
                )
            }

            composable(NavRoutes.PROFILE) {
                ProfileScreen()
            }
        }
    }
}
