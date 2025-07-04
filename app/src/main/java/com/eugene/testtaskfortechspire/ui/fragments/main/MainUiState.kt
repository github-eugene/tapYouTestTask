package com.eugene.testtaskfortechspire.ui.fragments.main

sealed class MainUiState {
    data object Loading : MainUiState()
    data object None: MainUiState()
}

