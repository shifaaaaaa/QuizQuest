package com.shifa.quizquest

import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.ServerTimestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

data class QuizResultData(
    val userId: String = "",
    val quizId: Int = 0,
    val quizTitle: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val timeTakenSeconds: Int? = null,
    @ServerTimestamp
    val completedAt: Date? = null
)

data class QuizResult(
    val id: String,
    val quizTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val completedAtFormatted: String,
    val percentage: Double,
    val performanceColor: Color,
    val performanceText: String?
)

fun QuizResultData.toUiModel(id: String): QuizResult {
    val percentage = if (this.totalQuestions > 0) (this.score.toDouble() / this.totalQuestions) * 100 else 0.0
    val performanceColor = when {
        percentage >= 90 -> Color(0xFF4CAF50)
        percentage >= 75 -> Color(0xFF8BC34A)
        percentage >= 50 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }
    val performanceText = when {
        percentage >= 90 -> "Luar Biasa!"
        percentage >= 75 -> "Kerja Bagus!"
        percentage >= 50 -> "Cukup Baik"
        else -> "Perlu Ditingkatkan"
    }
    val completedAtDate = this.completedAt ?: Date()
    val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
    val completedAtFormatted = dateFormat.format(completedAtDate)

    return QuizResult(
        id = id,
        quizTitle = this.quizTitle,
        score = this.score,
        totalQuestions = this.totalQuestions,
        completedAtFormatted = completedAtFormatted,
        percentage = (percentage * 100).roundToInt() / 100.0,
        performanceColor = performanceColor,
        performanceText = performanceText // Mengisi properti ini
    )
}