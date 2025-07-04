package com.eugene.testtaskfortechspire.ui.fragments.graph

import com.eugene.testtaskfortechspire.model.PointUiModel

data class GraphUiState(
    val points: List<PointUiModel>,
    val smoothLine : Boolean = false
)