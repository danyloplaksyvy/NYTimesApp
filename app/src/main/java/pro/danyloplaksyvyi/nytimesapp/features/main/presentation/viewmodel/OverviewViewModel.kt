package pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.OverviewRepository
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.Results

class OverviewViewModel(
    private val repository: OverviewRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<OverviewUiState>(OverviewUiState.Loading)
    val uiState: StateFlow<OverviewUiState> = _uiState

    fun loadOverview(date: String) {
        _uiState.value = OverviewUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.fetchOverview(date)
                _uiState.value = OverviewUiState.Success(response.results)
            } catch (e: Exception) {
                _uiState.value = OverviewUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}

sealed class OverviewUiState {
    object Loading : OverviewUiState()
    data class Success(val results: Results) : OverviewUiState()
    data class Error(val message: String) : OverviewUiState()
}