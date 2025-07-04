package com.eugene.data.repository

import com.eugene.data.api.ApiService
import com.eugene.domain.model.Point
import com.eugene.domain.repository.PointsRepository
import javax.inject.Inject

class PointsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PointsRepository {

    override suspend fun getPoints(count: Int): List<Point> {
        val response = apiService.getPoints(count)
        if (response.isSuccessful) {
            return response.body()?.points?.map {
                Point(it.x, it.y)
            } ?: emptyList()
        } else {
            throw Exception("Server error: ${response.code()}")
        }
    }
}