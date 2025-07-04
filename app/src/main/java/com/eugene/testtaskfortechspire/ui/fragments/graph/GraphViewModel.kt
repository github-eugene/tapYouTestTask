package com.eugene.testtaskfortechspire.ui.fragments.graph

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugene.testtaskfortechspire.model.PointUiModel
import com.eugene.testtaskfortechspire.usecase.SaveGraphUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GraphViewModel @Inject constructor(
    val saveGraphUseCase: SaveGraphUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(GraphUiState(listOf()))
    val stateFlow: StateFlow<GraphUiState> = _stateFlow

    private val _eventFlow = MutableSharedFlow<GraphUiEvent>()
    val eventFlow: SharedFlow<GraphUiEvent> = _eventFlow

    fun setPoints(pointsList: List<PointUiModel>) {
        _stateFlow.value = GraphUiState(points = pointsList)
    }

    fun setSmoothness(value: Boolean) {
        _stateFlow.value = _stateFlow.value.copy(smoothLine = value)
    }

    fun saveGraph(graphBitmap: Bitmap) {
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) { saveGraphUseCase(graphBitmap) }
            }.onSuccess { result ->
                _eventFlow.emit(GraphUiEvent.FileSaved)
            }.onFailure {
                _eventFlow.emit(GraphUiEvent.FileSaveError)
            }
        }
    }
}