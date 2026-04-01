package ru.nsu.salina.ui.screens.workouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.nsu.salina.domain.model.DayPlan
import ru.nsu.salina.ui.components.ExerciseListItem
import ru.nsu.salina.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailSheet(
    dayPlan: DayPlan,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "День ${dayPlan.day} • ${dayPlan.focus}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (dayPlan.exercises.isEmpty()) {
                Text(
                    text = "День отдыха — упражнений нет",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            } else {
                dayPlan.exercises.forEachIndexed { index, exercise ->
                    ExerciseListItem(index = index, exercise = exercise)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
