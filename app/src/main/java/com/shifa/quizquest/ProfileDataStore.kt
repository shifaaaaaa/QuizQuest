package com.shifa.quizquest.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.shifa.quizquest.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_profile")

class ProfileDataStore(private val context: Context) {
    companion object {
        val NICKNAME_KEY = stringPreferencesKey("nickname")
        val DESCRIPTION_KEY = stringPreferencesKey("description")
        val IMAGE_KEY = intPreferencesKey("image_id")
    }

    val nickname: Flow<String> = context.dataStore.data
        .map { it[NICKNAME_KEY] ?: "" }

    val description: Flow<String> = context.dataStore.data
        .map { it[DESCRIPTION_KEY] ?: "" }

    val imageId: Flow<Int> = context.dataStore.data
        .map { it[IMAGE_KEY] ?: R.drawable.profile1 }

    suspend fun saveProfile(nickname: String, description: String, imageId: Int) {
        context.dataStore.edit { preferences ->
            preferences[NICKNAME_KEY] = nickname
            preferences[DESCRIPTION_KEY] = description
            preferences[IMAGE_KEY] = imageId
        }
    }

    suspend fun getProfile(): ProfileData {
        val prefs = context.dataStore.data.first()
        val nickname = prefs[NICKNAME_KEY] ?: ""
        val description = prefs[DESCRIPTION_KEY] ?: ""
        val imageRes = prefs[IMAGE_KEY] ?: R.drawable.profile1
        return ProfileData(nickname, description, imageRes)
    }
}
