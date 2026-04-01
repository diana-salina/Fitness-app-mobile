package ru.nsu.salina.ui.screens.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.nsu.salina.data.repository.TrainingRepository
import ru.nsu.salina.domain.model.ActivePlan
import ru.nsu.salina.domain.model.DayPlan
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class WorkoutsUiState(
    val activePlan: ActivePlan? = null,
    val currentDayPlan: DayPlan? = null,
    val isTodayCompleted: Boolean = false,
    val snackbarMessage: String? = null,
    val isLoading: Boolean = true
)

class WorkoutsViewModel(private val repository: TrainingRepository) : ViewModel() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val _currentDayPlan = MutableStateFlow<DayPlan?>(null)
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    private var lastAdvanceCheckDate: String = ""

    val uiState: StateFlow<WorkoutsUiState> = combine(
        repository.getActivePlanFlow(),
        repository.getCompletedWorkoutsFlow(),
        _currentDayPlan,
        _snackbarMessage
    ) { plan, completedWorkouts, dayPlan, snackbar ->
        val activePlan = if (plan?.isCompleted == true) null else plan
        val today = LocalDate.now().format(formatter)
        val isTodayCompleted = activePlan?.let { p ->
            completedWorkouts.any { it.dayNumber == p.currentDay && it.date == today }
        } ?: false
        WorkoutsUiState(
            activePlan = activePlan,
            currentDayPlan = if (activePlan != null) dayPlan else null,
            isTodayCompleted = isTodayCompleted,
            snackbarMessage = snackbar,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WorkoutsUiState())

    fun refresh() {
        val today = LocalDate.now().format(formatter)
        if (today == lastAdvanceCheckDate) return
        lastAdvanceCheckDate = today
        viewModelScope.launch { repository.checkAndAdvanceDay() }
    }

    init {
        viewModelScope.launch {
            repository.getActivePlanFlow()
                .distinctUntilChanged()
                .collect { plan ->
                    if (plan != null && !plan.isCompleted) {
                        val dayPlan = repository.getCurrentDayPlan()
                        _currentDayPlan.value = dayPlan
                        val today = LocalDate.now().format(formatter)
                        if (dayPlan?.focus == "Отдых" && plan.restDayShownDate != today) {
                            repository.markRestDayShown()
                        }
                    } else {
                        _currentDayPlan.value = null
                    }
                }
        }
    }

    fun clearSnackbar() { _snackbarMessage.value = null }
    fun showSnackbar(message: String) { _snackbarMessage.value = message }

    companion object {
        fun factory(repository: TrainingRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { WorkoutsViewModel(repository) }
        }
    }
}
