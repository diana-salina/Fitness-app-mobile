package ru.nsu.salina.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.nsu.salina.data.repository.TrainingRepository
import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val displayedMonth: YearMonth = YearMonth.now(),
    val completedDates: Set<LocalDate> = emptySet(),
    val today: LocalDate = LocalDate.now()
)

class CalendarViewModel(private val repository: TrainingRepository) : ViewModel() {

    private val _displayedMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<CalendarUiState> = combine(
        _displayedMonth,
        repository.getCompletedDatesFlow()
    ) { month, dateStrings ->
        CalendarUiState(
            displayedMonth = month,
            completedDates = dateStrings.mapNotNull { str ->
                runCatching { LocalDate.parse(str) }.getOrNull()
            }.toSet(),
            today = LocalDate.now()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    fun previousMonth() { _displayedMonth.update { it.minusMonths(1) } }
    fun nextMonth() { _displayedMonth.update { it.plusMonths(1) } }

    companion object {
        fun factory(repository: TrainingRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { CalendarViewModel(repository) }
        }
    }
}
