package com.shifa.quizquest

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shifa.quizquest.datastore.ProfileDataStore
import com.shifa.quizquest.repository.ProfileRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun onUsernameChange(newUsername: String) {
        username = newUsername
        errorMessage = null
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        errorMessage = null
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        errorMessage = null
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        errorMessage = null
    }

    fun performSignup(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        errorMessage = null

        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Semua field harus diisi!"
            onFailure(errorMessage!!)
            return
        }

        if (password != confirmPassword) {
            errorMessage = "Password tidak cocok!"
            onFailure(errorMessage!!)
            return
        }

        if (password.length < 6) {
            errorMessage = "Password minimal 6 karakter!"
            onFailure(errorMessage!!)
            return
        }

        isLoading = true

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        viewModelScope.launch {
                            try {
                                val profileDataStore = ProfileDataStore(context, uid)
                                val repo = ProfileRepository(context, uid)

                                // 1. Simpan ke DataStore lokal
                                profileDataStore.saveProfile(
                                    nickname = username,
                                    description = "Pengguna Baru",
                                    imageId = R.drawable.profile1
                                )

                                // 2. Simpan juga ke Firestore
                                repo.syncProfileToCloud()

                                onSuccess()
                            } catch (e: Exception) {
                                errorMessage = "Gagal menyimpan data: ${e.message}"
                                onFailure(errorMessage!!)
                            }
                        }
                    } else {
                        errorMessage = "Sign Up gagal: UID tidak ditemukan"
                        onFailure(errorMessage!!)
                    }
                } else {
                    errorMessage = task.exception?.message ?: "Sign Up gagal"
                    onFailure(errorMessage!!)
                }
            }
        }
    }