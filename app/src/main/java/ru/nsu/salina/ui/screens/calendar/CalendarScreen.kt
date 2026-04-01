package ru.nsu.salina.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.nsu.salina.ui.theme.BackgroundWhite
import ru.nsu.salina.ui.theme.PrimaryBlue
import ru.nsu.salina.ui.theme.SurfaceBlue
import ru.nsu.salina.ui.theme.TextPrimary
import ru.nsu.salina.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(viewModel: CalendarViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(vertical = 8.dp)
    ) {
        MonthHeader(
            yearMonth = state.displayedMonth,
            onPrevious = viewModel::previousMonth,
            onNext = viewModel::nextMonth
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeekDaysHeader()
        Spacer(modifier = Modifier.height(4.dp))
        CalendarGrid(
            yearMonth = state.displayedMonth,
            completedDates = state.completedDates,
            today = state.today
        )
    }
}

@Composable
private fun MonthHeader(
    yearMonth: YearMonth,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevious) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Предыдущий месяц"
            )
        }
        val monthName = yearMonth.month
            .getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
            .replaceFirstChar { it.uppercase() }
        Text(
            text = "$monthName ${yearMonth.year}",
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(onClick = onNext) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Следующий месяц"
            )
        }
    }
}

@Composable
private fun WeekDaysHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    yearMonth: YearMonth,
    completedDates: Set<LocalDate>,
    today: LocalDate
) {
    val firstDay = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    // dayOfWeek: Monday=1, Sunday=7. Offset for Monday-first grid (0-based):
    val startOffset = firstDay.dayOfWeek.value - 1
    val totalRows = (startOffset + daysInMonth + 6) / 7

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        for (row in 0 until totalRows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0..6) {
                    val dayOfMonth = row * 7 + col - startOffset + 1
                    if (dayOfMonth < 1 || dayOfMonth > daysInMonth) {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        val date = yearMonth.atDay(dayOfMonth)
                        CalendarDayCell(
                            day = dayOfMonth,
                            isToday = date == today,
                            isCompleted = date in completedDates,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: Int,
    isToday: Boolean,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .then(
                        if (isToday) Modifier.background(SurfaceBlue, CircleShape)
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                    color = if (isToday) PrimaryBlue else TextPrimary
                )
            }
            // Reserve consistent space below for the dot
            Box(
                modifier = Modifier.height(8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(Color(0xFF13CF02), CircleShape)
                    )
                }
            }
        }
    }
}
