package com.shifa.quizquest

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/recent-quiz-results.php")
    suspend fun getRecentQuizResults(
        @Query("user_id") userId: Long,
        @Query("limit") limit: Int = 5
    ): Response<QuizResultResponse>

    @GET("api/quiz-results.php")
    suspend fun getAllQuizResults(
        @Query("user_id") userId: Long,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<QuizResultResponse>
}