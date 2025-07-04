package com.eugene.data.model

data class PointsResponse(
    val points: List<PointDto>
)

data class PointDto(
    val x: Double,
    val y: Double
)