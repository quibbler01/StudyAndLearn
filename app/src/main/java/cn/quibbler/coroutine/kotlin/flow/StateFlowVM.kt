package cn.quibbler.coroutine.kotlin.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class StateFlowVM : ViewModel() {

    private val _uiState = MutableSharedFlow<Int>(replay = 1, extraBufferCapacity = 32)
    val uiState: SharedFlow<Int> = _uiState.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            repeat(5) {
                _uiState.emit(it)
            }
        }
    }

}

sealed class LeastNewsUiState {
    data class Success(val news: List<String>) : LeastNewsUiState()
    data class Error(val exception: Throwable) : LeastNewsUiState()
}