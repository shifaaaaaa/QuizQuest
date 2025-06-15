package com.shifa.quizquest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        errorMessage = null

        // Validasi input
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
                    // Signup berhasil
                    onSuccess()
                } else {
                    // Signup gagal
                    errorMessage = task.exception?.message ?: "Signup gagal"
                    onFailure(errorMessage!!)
                }
            }
    }
}