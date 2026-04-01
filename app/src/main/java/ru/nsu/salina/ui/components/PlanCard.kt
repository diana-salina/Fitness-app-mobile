package ru.nsu.salina.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.nsu.salina.ui.theme.BackgroundWhite
import ru.nsu.salina.ui.theme.ButtonShape
import ru.nsu.salina.ui.theme.CardShape
import ru.nsu.salina.ui.theme.PrimaryBlue
import ru.nsu.salina.ui.theme.SurfaceBlue
import ru.nsu.salina.ui.theme.TextSecondary

@Composable
fun PlanCard(
    name: String,
    durationDays: Int,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceBlue, CardShape)
            .padding(16.dp)
    ) {
        Text(text = name, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$durationDays дней",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = onStart,
                shape = ButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Начать", color = BackgroundWhite)
            }
        }
    }
}
