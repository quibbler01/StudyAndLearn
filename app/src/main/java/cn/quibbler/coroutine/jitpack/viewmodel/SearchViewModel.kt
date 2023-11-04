package cn.quibbler.coroutine.jitpack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<String>("")
    val uiState = _uiState

    fun loadMessage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val string = "Message"
                delay(1000)
                _uiState.value = string
            }
        }
    }

    fun fetchData() {
        val job = viewModelScope.launch {

        }

        job.cancel()
    }

}