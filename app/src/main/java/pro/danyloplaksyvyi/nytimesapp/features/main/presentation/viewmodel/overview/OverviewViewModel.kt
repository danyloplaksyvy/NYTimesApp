package pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.overview.OverviewRepository
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.Results

class OverviewViewModel(
    private val repository: OverviewRepository
) : ViewModel() {
    private val tag = "OverviewViewModel"
    private val _uiState = MutableStateFlow<OverviewUiState>(OverviewUiState.Loading)
    val uiState: StateFlow<OverviewUiState> = _uiState

    fun loadOverview(date: String) {
        _uiState.value = OverviewUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getOverview(date)
                _uiState.value = OverviewUiState.Success(response.results)
            } catch (e: Exception) {
                _uiState.value = OverviewUiState.Error(e.localizedMessage ?: "Unknown error")
                e.localizedMessage?.let { Log.e(tag, it) }
            }
        }
    }
}

sealed class OverviewUiState {
    object Loading : OverviewUiState()
    data class Success(val results: Results) : OverviewUiState()
    data class Error(val message: String) : OverviewUiState()
}