package com.shifa.quizquest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    // State pesan error
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // memperbarui email
    fun onEmailChange(newEmail: String) {
        email = newEmail
        errorMessage = null
    }

    // memperbarui password
    fun onPasswordChange(newPassword: String) {
        password = newPassword
        errorMessage = null
    }

    // Fungsi logika login
    fun performLogin(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        errorMessage = null // Reset error message

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email dan Password tidak boleh kosong!"
            onFailure(errorMessage!!)
            return
        }
        if (email == "user@gmail.com" && password == "1234") {
            // Login sukses
            onSuccess()
        } else {
            errorMessage = "Email atau Password salah!"
            onFailure(errorMessage!!)
        }
    }
}