package cn.quibbler.coroutine.kotlin.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class StateFlowVM : ViewModel() {

    private val _uiState = MutableStateFlow(LeastNewsUiState.Success(emptyList()))
    val uiState: StateFlow<LeastNewsUiState> = _uiState

    init {
        viewModelScope.launch {
            delay(2000)
            _uiState.emit(LeastNewsUiState.Success(listOf("News One")))
        }
    }

}

sealed class LeastNewsUiState {
    data class Success(val news: List<String>) : LeastNewsUiState()
    data class Error(val exception: Throwable) : LeastNewsUiState()
}