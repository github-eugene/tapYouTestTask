package com.eugene.domain.repository

import com.eugene.domain.model.Point

interface PointsRepository {
    suspend fun getPoints(count: Int): List<Point>
}