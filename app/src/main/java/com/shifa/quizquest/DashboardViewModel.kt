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

    // StateFlow untuk hasil kuis terbaru
    val recentQuizResults: StateFlow<List<QuizResultData>> = flow {
        auth.currentUser?.uid?.let { userId ->
            val results = QuizRepository.getRecentResultsForUser(userId)
            emit(results)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}