package ru.nsu.salina.ui.screens.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.nsu.salina.data.repository.TrainingRepository
import ru.nsu.salina.domain.model.Exercise

data class ExecutionUiState(
    val exercises: List<Exercise> = emptyList(),
    val currentIndex: Int = 0,
    val dayFocus: String = "",
    val dayNumber: Int = 0,
    val isAllDone: Boolean = false,
    val isPlanComplete: Boolean = false,
    val isLoading: Boolean = true
)

class ExecutionViewModel(private val repository: TrainingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ExecutionUiState())
    val uiState: StateFlow<ExecutionUiState> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val dayPlan = repository.getCurrentDayPlan()
            val activePlan = repository.getActivePlanFlow().first()
            if (dayPlan != null && activePlan != null) {
                _uiState.value = ExecutionUiState(
                    exercises = dayPlan.exercises,
                    currentIndex = 0,
                    dayFocus = dayPlan.focus,
                    dayNumber = dayPlan.day,
                    isPlanComplete = activePlan.currentDay == 14,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun next() {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1
        _uiState.value = if (nextIndex >= state.exercises.size) {
            state.copy(isAllDone = true)
        } else {
            state.copy(currentIndex = nextIndex)
        }
    }

    fun skip() = next()

    suspend fun onWorkoutCompleted() {
        repository.completeCurrentDay()
        if (_uiState.value.isPlanComplete) {
            repository.finishActivePlan()
        }
    }

    companion object {
        fun factory(repository: TrainingRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { ExecutionViewModel(repository) }
        }
    }
}
