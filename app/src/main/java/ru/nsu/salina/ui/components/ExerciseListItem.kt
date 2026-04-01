package ru.nsu.salina.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.nsu.salina.domain.model.Exercise
import ru.nsu.salina.ui.theme.TextSecondary

@Composable
fun ExerciseListItem(
    index: Int,
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${index + 1}.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.width(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = exercise.name, style = MaterialTheme.typography.bodyMedium)
                val metric = when {
                    exercise.repetitions != null -> "${exercise.repetitions} повт."
                    exercise.duration != null -> "${exercise.duration} сек."
                    else -> ""
                }
                if (metric.isNotEmpty()) {
                    Text(
                        text = metric,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    }
}
