package com.shifa.quizquest

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shifa.quizquest.datastore.ProfileData
import com.shifa.quizquest.datastore.ProfileDataStore
import com.shifa.quizquest.repository.ProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.shifa.quizquest.R
import androidx.compose.runtime.State


class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val uid = Firebase.auth.currentUser?.uid ?: "default"
    private val dataStore = ProfileDataStore(application.applicationContext, uid)

    private val _isReady = mutableStateOf(false)
    val isReady: State<Boolean> get() = _isReady

    init {
        viewModelScope.launch {
            val uid = Firebase.auth.currentUser?.uid ?: return@launch
            ProfileRepository(getApplication(), uid).syncProfileToLocal()
            _isReady.value = true
        }
    }

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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileData("", "", R.drawable.profile1))
}
