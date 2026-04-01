package ru.nsu.salina.ui.screens.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nsu.salina.data.repository.TrainingRepository
import ru.nsu.salina.domain.model.PlanGenerationResult

sealed class PlanLoadingState {
    object Loading : PlanLoadingState()
    data class Done(val success: Boolean, val message: String) : PlanLoadingState()
}

class PlanLoadingViewModel(
    private val repository: TrainingRepository,
    private val contraindicationIds: List<Int>
) : ViewModel() {

    private val _state = MutableStateFlow<PlanLoadingState>(PlanLoadingState.Loading)
    val state: StateFlow<PlanLoadingState> = _state.asStateFlow()

    init {
        generatePlan()
    }

    private fun generatePlan() {
        viewModelScope.launch {
            val result = repository.generateAndSavePlan(contraindicationIds)
            _state.value = when (result) {
                is PlanGenerationResult.Success ->
                    PlanLoadingState.Done(success = true, message = "План успешно создан!")
                is PlanGenerationResult.Failure ->
                    PlanLoadingState.Done(success = false, message = result.message)
            }
        }
    }

    companion object {
        fun factory(
            repository: TrainingRepository,
            contraindicationIds: List<Int>
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer { PlanLoadingViewModel(repository, contraindicationIds) }
        }
    }
}
