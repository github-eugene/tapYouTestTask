package com.eugene.testtaskfortechspire.ui.fragments.main

import android.net.http.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugene.domain.usecase.GetPointsUseCase
import com.eugene.testtaskfortechspire.model.mappers.toUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getPointsUseCase: GetPointsUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<MainUiState>(MainUiState.None)
    val stateFlow: StateFlow<MainUiState> = _stateFlow

    private val _eventFlow = MutableSharedFlow<MainUiEvent>()
    val eventFlow: SharedFlow<MainUiEvent> = _eventFlow

    fun getPoints(count: Int) {
        viewModelScope.launch {
            if (!isValidCount(count)) {
                _eventFlow.emit(MainUiEvent.NotValidCountError)
                return@launch
            }
            _stateFlow.value = MainUiState.Loading
            val result = getPointsUseCase(count)
            result.onSuccess { points ->
                _stateFlow.value = MainUiState.None
                _eventFlow.emit(MainUiEvent.NavigateToGraph(points.toUiModel()))
            }.onFailure {
                when (result.exceptionOrNull()) {
                    is IOException -> _eventFlow.emit(MainUiEvent.NoInternetError)
                    else -> _eventFlow.emit(MainUiEvent.ServerError)
                }
                _stateFlow.value = MainUiState.None
            }
        }
    }

    private fun isValidCount(count: Int): Boolean {
        return count in 1..1000
    }
}