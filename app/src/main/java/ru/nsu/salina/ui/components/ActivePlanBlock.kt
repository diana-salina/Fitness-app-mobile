package ru.nsu.salina.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.nsu.salina.ui.theme.ButtonShape
import ru.nsu.salina.ui.theme.CardShape
import ru.nsu.salina.ui.theme.DisabledGray
import ru.nsu.salina.ui.theme.PrimaryBlue
import ru.nsu.salina.ui.theme.SurfaceBlue
import ru.nsu.salina.ui.theme.TextSecondary

enum class DayButtonState { Start, Completed, Rest }

@Composable
fun ActivePlanBlock(
    planName: String,
    currentDay: Int,
    totalDays: Int = 14,
    dayFocus: String?,
    buttonState: DayButtonState,
    onDetails: () -> Unit,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceBlue, CardShape)
            .padding(16.dp)
    ) {
        Text(text = planName, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "День $currentDay из $totalDays${if (dayFocus != null) " • $dayFocus" else ""}",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            OutlinedButton(
                onClick = onDetails,
                shape = ButtonShape
            ) {
                Text("Детали")
            }
            Spacer(modifier = Modifier.width(8.dp))
            when (buttonState) {
                DayButtonState.Start -> Button(
                    onClick = onStart,
                    shape = ButtonShape,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Начать")
                }
                DayButtonState.Completed -> Button(
                    onClick = {},
                    enabled = false,
                    shape = ButtonShape
                ) {
                    Text("Выполнено")
                }
                DayButtonState.Rest -> Button(
                    onClick = {},
                    enabled = false,
                    shape = ButtonShape,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = DisabledGray.copy(alpha = 0.3f)
                    )
                ) {
                    Text("Отдых")
                }
            }
        }
    }
}
