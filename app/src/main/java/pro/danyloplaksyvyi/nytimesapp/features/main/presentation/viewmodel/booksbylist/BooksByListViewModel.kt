package pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.booksbylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.booksbylist.BooksByListRepository
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist.ListResults

class BooksByListViewModel(
    private val repository: BooksByListRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<BookListUiState>(BookListUiState.Loading)
    val uiState: StateFlow<BookListUiState> = _uiState

    fun loadList(date: String, listName: String) = viewModelScope.launch {
        _uiState.value = BookListUiState.Loading
        _uiState.value = try {
            val resp = repository.getBooksForList(date, listName)
            BookListUiState.Success(resp.results)
        } catch (t: Throwable) {
            BookListUiState.Error(t.message ?: "Unknown error")
        }
    }
}

sealed class BookListUiState {
    object Loading : BookListUiState()
    data class Success(val data: ListResults) : BookListUiState()
    data class Error(val message: String) : BookListUiState()
}