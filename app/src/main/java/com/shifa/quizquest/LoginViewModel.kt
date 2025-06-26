package com.shifa.quizquest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.shifa.quizquest.datastore.ProfileData
import com.shifa.quizquest.datastore.ProfileDataStore
import com.shifa.quizquest.repository.ProfileRepository
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    var profileData by mutableStateOf<ProfileData?>(null)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
        errorMessage = null
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        errorMessage = null
    }

    fun performLogin(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        errorMessage = null

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email dan Password tidak boleh kosong!"
            onFailure(errorMessage!!)
            return
        }

        isLoading = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        viewModelScope.launch {
                            try {
                                // 1. Sinkronkan dari cloud ke lokal
                                val repo = ProfileRepository(context, uid)
                                repo.syncProfileToLocal()

                                // 2. Ambil dari lokal ke ViewModel
                                val store = ProfileDataStore(context, uid)
                                profileData = store.getProfile()

                                onSuccess()
                            } catch (e: Exception) {
                                errorMessage = "Gagal memuat profil: ${e.message}"
                                onFailure(errorMessage!!)
                            }
                        }
                    } else {
                        errorMessage = "Gagal memuat UID user"
                        onFailure(errorMessage!!)
                    }
                } else {
                    errorMessage = task.exception?.message ?: "Login gagal"
                    onFailure(errorMessage!!)
                }
            }
    }


    fun signOut() {
        auth.signOut()
    }
}