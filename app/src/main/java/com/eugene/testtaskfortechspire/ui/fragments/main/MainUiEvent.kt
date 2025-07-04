package com.eugene.testtaskfortechspire.ui.fragments.main

import com.eugene.testtaskfortechspire.model.PointUiModel

sealed class MainUiEvent {
    data class NavigateToGraph(val points: List<PointUiModel>) : MainUiEvent()
    data object NoInternetError : MainUiEvent()
    data object ServerError : MainUiEvent()
    data object NotValidCountError : MainUiEvent()
}