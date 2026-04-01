package ru.nsu.salina.ui.screens.workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.nsu.salina.ui.components.ActivePlanBlock
import ru.nsu.salina.ui.components.DayButtonState
import ru.nsu.salina.ui.components.PlanCard
import ru.nsu.salina.ui.navigation.NavRoutes

@Composable
fun WorkoutsScreen(
    viewModel: WorkoutsViewModel,
    navController: NavController,
    showDetailSheet: Boolean,
    onShowDetail: () -> Unit,
    onDismissDetail: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refresh()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Handle plan result from PlanLoadingScreen via savedStateHandle
    val planResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("planResult", null)
        ?.collectAsState()

    LaunchedEffect(planResult?.value) {
        val msg = planResult?.value
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("planResult")
        }
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val activePlan = uiState.activePlan
            if (activePlan != null) {
                item {
                    val dayFocus = uiState.currentDayPlan?.focus
                    val buttonState = when {
                        dayFocus == "Отдых" -> DayButtonState.Rest
                        uiState.isTodayCompleted -> DayButtonState.Completed
                        else -> DayButtonState.Start
                    }
                    ActivePlanBlock(
                        planName = activePlan.name,
                        currentDay = activePlan.currentDay,
                        dayFocus = dayFocus,
                        buttonState = buttonState,
                        onDetails = onShowDetail,
                        onStart = { navController.navigate(NavRoutes.WORKOUT_EXECUTION) }
                    )
                }
            }

            item {
                Text(
                    text = "Другие планы",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = if (activePlan != null) 8.dp else 0.dp)
                )
            }

            item {
                PlanCard(
                    name = "14-дневный курс",
                    durationDays = 14,
                    onStart = { navController.navigate(NavRoutes.CONTRAINDICATIONS) }
                )
            }
        }

        // Detail bottom sheet
        if (showDetailSheet) {
            uiState.currentDayPlan?.let { dayPlan ->
                WorkoutDetailSheet(
                    dayPlan = dayPlan,
                    onDismiss = onDismissDetail
                )
            }
        }
    }
}
