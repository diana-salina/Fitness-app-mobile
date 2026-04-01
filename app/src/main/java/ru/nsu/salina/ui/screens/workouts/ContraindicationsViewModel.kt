package ru.nsu.salina.ui.screens.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.nsu.salina.data.repository.TrainingRepository
import ru.nsu.salina.domain.model.Contraindication

data class ContraindicationsUiState(
    val allItems: List<Contraindication> = emptyList(),
    val filteredItems: List<Contraindication> = emptyList(),
    val selectedIds: Set<Int> = emptySet(),
    val query: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)

class ContraindicationsViewModel(private val repository: TrainingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ContraindicationsUiState())
    val uiState: StateFlow<ContraindicationsUiState> = _uiState.asStateFlow()

    init {
        loadContraindications()
    }

    fun loadContraindications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getContraindications()
                .onSuccess { items ->
                    _uiState.update {
                        it.copy(allItems = items, filteredItems = items, isLoading = false)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "Не удалось загрузить данные")
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { state ->
            val filtered = if (query.isBlank()) state.allItems
            else state.allItems.filter { it.name.contains(query, ignoreCase = true) }
            state.copy(query = query, filteredItems = filtered)
        }
    }

    fun toggleSelection(contraindication: Contraindication) {
        _uiState.update { state ->
            val newSelected = if (contraindication.id in state.selectedIds) {
                state.selectedIds - contraindication.id
            } else {
                state.selectedIds + contraindication.id
            }
            state.copy(selectedIds = newSelected)
        }
    }

    fun getSelectedIds(): List<Int> = _uiState.value.selectedIds.toList()

    companion object {
        fun factory(repository: TrainingRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { ContraindicationsViewModel(repository) }
        }
    }
}
