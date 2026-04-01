package ru.nsu.salina.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.nsu.salina.domain.model.Exercise
import ru.nsu.salina.ui.theme.CardShape
import ru.nsu.salina.ui.theme.PrimaryBlue
import ru.nsu.salina.ui.theme.SurfaceBlue
import ru.nsu.salina.ui.theme.TextSecondary

@Composable
fun ExerciseDetailCard(
    exercise: Exercise,
    currentIndex: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceBlue, CardShape)
            .padding(20.dp)
    ) {
        Text(
            text = "Упражнение ${currentIndex + 1} из $totalCount",
            style = MaterialTheme.typography.labelSmall,
            color = PrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        val metric = when {
            exercise.repetitions != null -> "${exercise.repetitions} повторений"
            exercise.duration != null -> "${exercise.duration} секунд"
            else -> ""
        }
        if (metric.isNotEmpty()) {
            Text(text = metric, style = MaterialTheme.typography.bodyMedium, color = PrimaryBlue)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Техника выполнения",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = exercise.technique, style = MaterialTheme.typography.bodyMedium)
    }
}
