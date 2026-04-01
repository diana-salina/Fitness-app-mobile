package ru.nsu.salina.ui.screens.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nsu.salina.ui.theme.BackgroundWhite
import ru.nsu.salina.ui.theme.ButtonShape
import ru.nsu.salina.ui.theme.PrimaryBlue
import ru.nsu.salina.ui.theme.SecondaryPurple
import ru.nsu.salina.ui.theme.TextSecondary

@Composable
fun CompletionScreen(
    isPlanComplete: Boolean,
    onDone: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text(
                text = if (isPlanComplete) "🏆" else "✅",
                fontSize = 56.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (isPlanComplete) "Курс завершён!" else "Отличная работа!",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = if (isPlanComplete) SecondaryPurple else PrimaryBlue
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isPlanComplete)
                    "Поздравляем! Вы успешно завершили 14-дневный курс. Это отличный результат!"
                else
                    "Тренировка выполнена. Возвращайтесь завтра!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onDone,
                shape = ButtonShape,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Готово")
            }
        }
    }
}
