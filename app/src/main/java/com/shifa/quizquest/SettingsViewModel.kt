package com.shifa.quizquest.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class SettingsViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // Password change fields
    var currentPasswordForPassword by mutableStateOf("")
    var newPassword by mutableStateOf("")

    // UI states
    var message by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun changePassword(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = auth.currentUser
        val currentEmail = user?.email

        if (user == null || currentEmail.isNullOrEmpty()) {
            onFailure("User tidak ditemukan.")
            return
        }

        if (currentPasswordForPassword.isBlank() || newPassword.isBlank()) {
            onFailure("Field password tidak boleh kosong.")
            return
        }

        val credential = EmailAuthProvider.getCredential(currentEmail, currentPasswordForPassword)

        isLoading = true
        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        isLoading = false
                        onSuccess()
                    }
                    .addOnFailureListener {
                        isLoading = false
                        onFailure("Gagal ubah password: ${it.message}")
                    }
            }
            .addOnFailureListener {
                isLoading = false
                onFailure("Re-auth gagal: ${it.message}")
            }
    }
}