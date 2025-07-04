package com.eugene.testtaskfortechspire.model.mappers

import com.eugene.domain.model.Point
import com.eugene.testtaskfortechspire.model.PointUiModel

fun List<Point>.toUiModel(): List<PointUiModel> {
    return this.map { point ->
        PointUiModel(
            x = point.x.toFloat(),
            y = point.y.toFloat()
        )
    }
}