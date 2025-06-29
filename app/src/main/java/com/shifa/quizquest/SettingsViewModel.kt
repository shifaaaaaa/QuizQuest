package com.shifa.quizquest.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var currentEmail by mutableStateOf("")
    var newEmail by mutableStateOf("")
    var oldPassword by mutableStateOf("")
    var newPassword by mutableStateOf("")

    var message by mutableStateOf<String?>(null)
    var loading by mutableStateOf(false)

    fun changeEmail(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(currentEmail, oldPassword)

        if (user != null) {
            loading = true
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updateEmail(newEmail).addOnCompleteListener { updateTask ->
                        loading = false
                        if (updateTask.isSuccessful) {
                            message = "Email berhasil diubah."
                            onSuccess()
                        } else {
                            message = updateTask.exception?.message
                            onFailure(message ?: "Gagal mengubah email.")
                        }
                    }
                } else {
                    loading = false
                    message = reauthTask.exception?.message
                    onFailure(message ?: "Re-autentikasi gagal.")
                }
            }
        } else {
            onFailure("User tidak terautentikasi.")
        }
    }

    fun changePassword(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(currentEmail, oldPassword)

        if (user != null) {
            loading = true
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        loading = false
                        if (updateTask.isSuccessful) {
                            message = "Password berhasil diubah."
                            onSuccess()
                        } else {
                            message = updateTask.exception?.message
                            onFailure(message ?: "Gagal mengubah password.")
                        }
                    }
                } else {
                    loading = false
                    message = reauthTask.exception?.message
                    onFailure(message ?: "Re-autentikasi gagal.")
                }
            }
        } else {
            onFailure("User tidak terautentikasi.")
        }
    }
}
