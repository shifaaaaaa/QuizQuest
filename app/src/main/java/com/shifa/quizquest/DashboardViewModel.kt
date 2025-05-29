package com.shifa.quizquest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shifa.quizquest.datastore.ProfileData
import com.shifa.quizquest.datastore.ProfileDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = ProfileDataStore(application)

    val profileData: StateFlow<ProfileData> = combine(
        dataStore.nickname,
        dataStore.description,
        dataStore.imageId
    ) { nickname, description, imageId ->
        ProfileData(nickname, description, imageId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileData("", "", R.drawable.profile1))
}
