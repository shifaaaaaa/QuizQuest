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

    // Daftar gambar valid
    private val validImages = listOf(
        R.drawable.profile1,
        R.drawable.profile2,
        R.drawable.profile3,
        R.drawable.profile4,
        R.drawable.profile5
    )

    val profileData: StateFlow<ProfileData> = combine(
        dataStore.nickname,
        dataStore.description,
        dataStore.imageId
    ) { nickname, description, imageId ->
        val safeImageId = if (imageId in validImages) imageId else R.drawable.profile1
        ProfileData(nickname, description, safeImageId)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProfileData("", "", R.drawable.profile1)
    )
}
