package com.shifa.quizquest.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.shifa.quizquest.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import com.google.firebase.auth.FirebaseAuth

private val Context.dataStore by preferencesDataStore(name = "user_profile_V2")

class ProfileDataStore(private val context: Context, private val uid: String) {
    companion object {
        private fun nicknameKey(uid: String) = stringPreferencesKey("nickname_$uid")
        private fun descriptionKey(uid: String) = stringPreferencesKey("description_$uid")
        private fun imageKey(uid: String) = intPreferencesKey("image_id_$uid")
    }

    val nickname: Flow<String> = context.dataStore.data
        .map { it[nicknameKey(uid)] ?: "" }

    val description: Flow<String> = context.dataStore.data
        .map { it[descriptionKey(uid)] ?: "" }

    val imageId: Flow<Int> = context.dataStore.data
        .map { it[imageKey(uid)] ?: R.drawable.profile1 }

    suspend fun saveProfile(nickname: String, description: String, imageId: Int) {
        context.dataStore.edit { preferences ->
            preferences[nicknameKey(uid)] = nickname
            preferences[descriptionKey(uid)] = description
            preferences[imageKey(uid)] = imageId

        }
    }

    suspend fun getProfile(): ProfileData {
        val prefs = context.dataStore.data.first()
        val index = prefs[imageKey(uid)] ?: 1
        return ProfileData(
            nickname = prefs[nicknameKey(uid)] ?: "",
            description = prefs[descriptionKey(uid)] ?: "",
            imageRes = indexToImageRes(index)
        )
    }
}

private fun imageResToIndex(resId: Int): Int {
    return when (resId) {
        R.drawable.profile1 -> 1
        R.drawable.profile2 -> 2
        R.drawable.profile3 -> 3
        R.drawable.profile4 -> 4
        R.drawable.profile5 -> 5
        else -> 1
    }
}

private fun indexToImageRes(index: Int): Int {
    return when (index) {
        1 -> R.drawable.profile1
        2 -> R.drawable.profile2
        3 -> R.drawable.profile3
        4 -> R.drawable.profile4
        5 -> R.drawable.profile5
        else -> R.drawable.profile1
    }
}