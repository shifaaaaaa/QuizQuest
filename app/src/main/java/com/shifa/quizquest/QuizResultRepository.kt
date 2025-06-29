package com.shifa.quizquest

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


object QuizResultRepository {
    suspend fun getRecentQuizResults(userId: String, limit: Long = 5): Result<List<QuizResultData>> {
        return try {
            val db = Firebase.firestore
            val results = mutableListOf<QuizResultData>()
            val querySnapshot = db.collection("quizResults")
                .whereEqualTo("userId", userId)
                .orderBy("completedAt", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.toObject(QuizResultData::class.java)?.let {
                    results.add(it)
                }
            }
            Result.success(results)
        } catch (e: Exception) {
            println("Error fetching recent quiz results: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getAllQuizResults(userId: String): Result<List<QuizResultData>> {
        return try {
            val db = Firebase.firestore
            val results = mutableListOf<QuizResultData>()
            val querySnapshot = db.collection("quizResults")
                .whereEqualTo("userId", userId)
                .orderBy("completedAt", Query.Direction.DESCENDING)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.toObject(QuizResultData::class.java)?.let {
                    results.add(it)
                }
            }
            Result.success(results)
        } catch (e: Exception) {
            println("Error fetching all quiz results: ${e.message}")
            Result.failure(e)
        }
    }
}