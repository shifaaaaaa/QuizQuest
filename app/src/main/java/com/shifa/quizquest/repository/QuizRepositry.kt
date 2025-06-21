package com.shifa.quizquest.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val TAG = "QuizRepository"

    suspend fun getQuizThemes(): Result<List<QuizTheme>> {
        return try {
            Log.d(TAG, "Fetching quiz themes from Firestore...")

            val snapshot = firestore.collection("quiz_themes")
                .get()
                .await()

            val themes = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(QuizTheme::class.java)?.copy(id = doc.id)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing theme document ${doc.id}", e)
                    null
                }
            }

            Log.d(TAG, "Successfully fetched ${themes.size} quiz themes")
            Result.success(themes)

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching quiz themes", e)
            Result.failure(e)
        }
    }

    suspend fun getQuestionsByTheme(themeId: String): Result<List<Question>> {
        return try {
            Log.d(TAG, "Fetching questions for theme: $themeId")

            val snapshot = firestore.collection("quiz_themes")
                .document(themeId)
                .collection("questions")
                .get()
                .await()

            val questions = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Question::class.java)?.copy(id = doc.id)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing question document ${doc.id}", e)
                    null
                }
            }

            Log.d(TAG, "Successfully fetched ${questions.size} questions for theme $themeId")
            Result.success(questions)

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching questions for theme $themeId", e)
            Result.failure(e)
        }
    }

    suspend fun saveQuizResult(userId: String, themeId: String, result: QuizResult): Result<Unit> {
        return try {
            Log.d(TAG, "Saving quiz result for user: $userId, theme: $themeId")

            val resultData = hashMapOf(
                "score" to result.score,
                "totalQuestions" to result.totalQuestions,
                "correctAnswers" to result.correctAnswers,
                "timeSpent" to result.timeSpent,
                "themeId" to themeId,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(userId)
                .collection("quiz_results")
                .add(resultData)
                .await()

            Log.d(TAG, "Successfully saved quiz result")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e(TAG, "Error saving quiz result", e)
            Result.failure(e)
        }
    }
}