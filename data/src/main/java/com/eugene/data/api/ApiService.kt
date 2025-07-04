package com.eugene.data.api

import com.eugene.data.model.PointsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/test/points")
    suspend fun getPoints(
        @Query("count") count: Int
    ): Response<PointsResponse>
}