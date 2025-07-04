package com.eugene.testtaskfortechspire.ui.fragments.graph

sealed class GraphUiEvent {
    object FileSaved: GraphUiEvent()
    object FileSaveError: GraphUiEvent()
}