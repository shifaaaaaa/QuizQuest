package com.shifa.quizquest

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignupViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

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
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        val userProfile = hashMapOf(
                            "uid" to firebaseUser.uid,
                            "username" to username,
                            "email" to email,
                            "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                        )

                        db.collection("users").document(firebaseUser.uid)
                            .set(userProfile)
                            .addOnSuccessListener {
                                isLoading = false
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                errorMessage = "Gagal menyimpan data profil: ${e.message}"
                                onFailure(errorMessage!!)
                            }
                    } else {
                        isLoading = false
                        errorMessage = "Gagal mendapatkan data pengguna setelah pendaftaran."
                        onFailure(errorMessage!!)
                    }
                } else {
                    isLoading = false
                    errorMessage = task.exception?.message ?: "Signup gagal"
                    onFailure(errorMessage!!)
                }
            }
        }
    }