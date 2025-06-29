package com.shifa.quizquest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shifa.quizquest.datastore.ProfileData
import com.shifa.quizquest.datastore.ProfileDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

data class DashboardStats(
    val quizzesTaken: Int = 0,
    val averageScore: Int = 0,
    val accuracy: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = Firebase.auth

    private val userIdFlow: StateFlow<String?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(authStateListener)

        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), auth.currentUser?.uid)


    val profileData: StateFlow<ProfileData> = userIdFlow.flatMapLatest { userId ->
        if (userId == null) {
            flowOf(ProfileData("", "", R.drawable.profile1))
        } else {
            val dataStore = ProfileDataStore(application, userId)
            combine(
                dataStore.nickname,
                dataStore.description,
                dataStore.imageId
            ) { nickname, description, imageId ->

                val validImages = listOf(
                    R.drawable.profile1,
                    R.drawable.profile2,
                    R.drawable.profile3,
                    R.drawable.profile4,
                    R.drawable.profile5
                )

                val safeImageId = if (validImages.contains(imageId)) {
                    imageId
                } else {
                    R.drawable.profile1
                }

                ProfileData(nickname, description, safeImageId)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileData("", "", R.drawable.profile1))

    val recentQuizResults: StateFlow<List<QuizResultData>> = userIdFlow.flatMapLatest { userId ->
        if (userId == null) {
            flowOf(emptyList())
        } else {
            flow { emit(QuizRepository.getRecentResultsForUser(userId)) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val dashboardStats: StateFlow<DashboardStats> = userIdFlow.flatMapLatest { userId ->
        if (userId == null) {
            flowOf(DashboardStats())
        } else {
            flow {
                val allResults = QuizRepository.getAllResultsForUser(userId)
                if (allResults.isNotEmpty()) {
                    val quizzesTaken = allResults.size
                    val totalCorrectAnswers = allResults.sumOf { it.score }
                    val totalQuestions = allResults.sumOf { it.totalQuestions }
                    val accuracy = if (totalQuestions > 0) (totalCorrectAnswers.toDouble() * 100 / totalQuestions).toInt() else 0
                    val averageScore = allResults.map { if (it.totalQuestions > 0) (it.score.toDouble() * 100 / it.totalQuestions) else 0.0 }.average().toInt()
                    emit(DashboardStats(quizzesTaken, averageScore, accuracy))
                } else {
                    emit(DashboardStats())
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardStats())
}