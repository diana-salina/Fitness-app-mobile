package ru.nsu.salina.ui.screens.workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.nsu.salina.ui.components.ExerciseDetailCard
import ru.nsu.salina.ui.components.LoadingScreen
import ru.nsu.salina.ui.navigation.NavRoutes
import ru.nsu.salina.ui.theme.ButtonShape
import ru.nsu.salina.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutExecutionScreen(
    viewModel: ExecutionViewModel,
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isAllDone) {
        if (state.isAllDone) {
            viewModel.onWorkoutCompleted()
            navController.navigate(NavRoutes.completion(state.isPlanComplete)) {
                popUpTo(NavRoutes.WORKOUTS) { inclusive = false }
            }
        }
    }

    if (state.isLoading) {
        LoadingScreen()
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.dayFocus.ifBlank { "Тренировка" }) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.exercises.isNotEmpty()) {
                ExerciseDetailCard(
                    exercise = state.exercises[state.currentIndex],
                    currentIndex = state.currentIndex,
                    totalCount = state.exercises.size,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.skip() },
                        modifier = Modifier.weight(1f),
                        shape = ButtonShape
                    ) {
                        Text("Пропустить")
                    }
                    Button(
                        onClick = { viewModel.next() },
                        modifier = Modifier.weight(1f),
                        shape = ButtonShape,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        val isLast = state.currentIndex == state.exercises.lastIndex
                        Text(if (isLast) "Завершить" else "Следующее")
                    }
                }
            }
        }
    }
}
