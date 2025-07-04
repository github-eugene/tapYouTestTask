package com.eugene.domain.usecase

import com.eugene.domain.model.Point
import com.eugene.domain.repository.PointsRepository

class GetPointsUseCase(
    private val repository: PointsRepository
) {
    suspend operator fun invoke(count: Int): Result<List<Point>> {
        return try {
            val points = repository.getPoints(count)
            Result.success(points)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}