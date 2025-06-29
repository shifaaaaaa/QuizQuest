package com.shifa.quizquest

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object QuizResultRepository {

    // Method untuk menyimpan hasil kuis - TAMBAHAN
    suspend fun saveQuizResult(quizResult: QuizResultData): Result<String> {
        return try {
            val db = Firebase.firestore
            val docRef = db.collection("quizResults").add(quizResult).await()
            println("Quiz result saved with ID: ${docRef.id}")
            Result.success(docRef.id)
        } catch (e: Exception) {
            println("Error saving quiz result: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getRecentQuizResults(userId: String, limit: Long = 5): Result<List<QuizResultData>> {
        return try {
            val db = Firebase.firestore
            val results = mutableListOf<QuizResultData>()

            println("Querying quizResults for userId: $userId") // Debug log

            val querySnapshot = db.collection("quizResults")
                .whereEqualTo("userId", userId)
                .orderBy("completedAt", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            println("Query returned ${querySnapshot.documents.size} documents") // Debug log

            for (document in querySnapshot.documents) {
                println("Processing document: ${document.id}") // Debug log
                document.toObject(QuizResultData::class.java)?.let { quizResult ->
                    println("Converted quiz result: ${quizResult.quizTitle}") // Debug log
                    results.add(quizResult)
                }
            }

            println("Final results count: ${results.size}") // Debug log
            Result.success(results)
        } catch (e: Exception) {
            println("Error fetching recent quiz results: ${e.message}")
            e.printStackTrace() // Print full stack trace
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

    // Method untuk debug - melihat semua data di collection
    suspend fun debugGetAllQuizResults(): Result<List<Map<String, Any?>>> {
        return try {
            val db = Firebase.firestore
            val results = mutableListOf<Map<String, Any?>>()
            val querySnapshot = db.collection("quizResults")
                .get()
                .await()

            for (document in querySnapshot.documents) {
                results.add(document.data ?: emptyMap())
            }
            println("Debug: Found ${results.size} total quiz results in collection")
            Result.success(results)
        } catch (e: Exception) {
            println("Error in debug query: ${e.message}")
            Result.failure(e)
        }
    }
}