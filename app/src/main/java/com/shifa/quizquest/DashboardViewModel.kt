package com.shifa.quizquest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shifa.quizquest.datastore.ProfileData
import com.shifa.quizquest.datastore.ProfileDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = Firebase.auth

    private val dataStore = ProfileDataStore(application, auth.currentUser?.uid ?: "default_uid")

    // StateFlow untuk ProfileData
    val profileData: StateFlow<ProfileData> = combine(
        dataStore.nickname,
        dataStore.description,
        dataStore.imageId
    ) { nickname, description, imageId ->
        val validImages = listOf(
            R.drawable.profile1, R.drawable.profile2,
            R.drawable.profile3, R.drawable.profile4, R.drawable.profile5
        )
        val safeImage = if (imageId in validImages) imageId else R.drawable.profile1
        ProfileData(nickname, description, safeImage)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileData("", "", R.drawable.profile1)
    )

    // StateFlow untuk hasil kuis terbaru - DIPERBAIKI
    val recentQuizResults: StateFlow<List<QuizResultData>> = flow {
        try {
            auth.currentUser?.uid?.let { userId ->
                println("Fetching quiz results for user: $userId")
                val result = QuizResultRepository.getRecentQuizResults(userId, 5)
                if (result.isSuccess) {
                    val results = result.getOrNull() ?: emptyList()
                    println("Found ${results.size} quiz results")
                    emit(results)
                } else {
                    println("Failed to fetch quiz results: ${result.exceptionOrNull()?.message}")
                    emit(emptyList<QuizResultData>())
                }
            } ?: run {
                println("No authenticated user found")
                emit(emptyList<QuizResultData>())
            }
        } catch (e: Exception) {
            println("Error in recentQuizResults flow: ${e.message}")
            emit(emptyList<QuizResultData>())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Method untuk refresh data secara manual
    fun refreshQuizResults() {
        viewModelScope.launch {
            try {
                auth.currentUser?.uid?.let { userId ->
                    val result = QuizResultRepository.getRecentQuizResults(userId, 5)
                    // Flow akan otomatis update karena menggunakan flow builder
                }
            } catch (e: Exception) {
                println("Error refreshing quiz results: ${e.message}")
            }
        }
    }
}
