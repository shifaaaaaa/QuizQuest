package com.shifa.quizquest

data class QuizResult(
    val id: Long,
    val userId: Long,
    val quizId: String,
    val score: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val timeTaken: Int?,
    val completedAt: String,
    val completedAtFormatted: String,
    val percentage: Double,
    val performanceLevel: String,
    val performanceColor: String,
    val performanceText: String? = null
)

data class QuizResultResponse(
    val success: Boolean,
    val message: String,
    val data: List<QuizResult>,
    val meta: Meta? = null
)

data class Meta(
    val count: Int,
    val userId: Long,
    val timestamp: String
)