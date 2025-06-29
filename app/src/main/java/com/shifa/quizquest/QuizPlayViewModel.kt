package com.shifa.quizquest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class QuizPlayViewModel : ViewModel() {

    private val _finalResult = MutableStateFlow<QuizResult?>(null)
    val finalResult = _finalResult.asStateFlow()

    fun saveQuizResult(
        quizId: Int,
        quizInfo: String,
        score: Int,
        totalQuestions: Int,
        startTime: Long
    ) {
        Log.d("SAVE_PROCESS", "Fungsi saveQuizResult di ViewModel TELAH DIPANGGIL.")
        viewModelScope.launch {
            val quizEndTime = System.currentTimeMillis()
            val timeTakenSeconds = TimeUnit.MILLISECONDS.toSeconds(quizEndTime - startTime).toInt()
            val user = Firebase.auth.currentUser

            if (user != null) {
                val resultData = QuizResultData(
                    userId = user.uid,
                    quizId = quizId,
                    quizTitle = quizInfo,
                    score = score,
                    totalQuestions = totalQuestions,
                    timeTakenSeconds = timeTakenSeconds
                )
                Log.d("VIEWMODEL_SAVE", "Memanggil QuizRepository.saveQuizResult dengan data: $resultData")
                QuizRepository.saveQuizResult(resultData)
                // Proses simpan sekarang aman di dalam viewModelScope
                _finalResult.value = resultData.toUiModel("local_id")
                // Setelah disimpan, update state agar UI bisa menampilkannya
                _finalResult.value = resultData.toUiModel("local_id")
            } else {
                Log.e("VIEWMODEL_SAVE", "Gagal menyimpan, user tidak ditemukan.")
            }
        }
    }

    // Fungsi untuk mereset state saat "Main Lagi"
    fun resetQuiz() {
        _finalResult.value = null
    }
}