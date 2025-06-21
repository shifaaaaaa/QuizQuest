package com.shifa.quizquest.models

data class QuizTheme(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val questionCount: Int = 0,
    val difficulty: String = "Easy",
    val category: String = ""
)

data class Question(
    val id: String = "",
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: Int = 0,
    val explanation: String = "",
    val points: Int = 10
)

data class QuizResult(
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val timeSpent: Long = 0
)

