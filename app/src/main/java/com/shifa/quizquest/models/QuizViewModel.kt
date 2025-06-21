package com.shifa.quizquest.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shifa.quizquest.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun loadQuiz(themeId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            quizRepository.getQuestionsByTheme(themeId)
                .onSuccess { questions ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        questions = questions.shuffled(), // Acak urutan soal
                        currentQuestionIndex = 0,
                        totalQuestions = questions.size,
                        error = if (questions.isEmpty()) "Tidak ada soal tersedia untuk tema ini" else null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Gagal memuat soal: ${exception.message}"
                    )
                }
        }
    }

    fun selectAnswer(answerIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedAnswer = answerIndex)
    }

    fun nextQuestion() {
        val currentState = _uiState.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex)

        if (currentQuestion != null && currentState.selectedAnswer != -1) {
            val isCorrect = currentState.selectedAnswer == currentQuestion.correctAnswer
            val newScore = if (isCorrect) currentState.score + currentQuestion.points else currentState.score
            val newCorrectAnswers = if (isCorrect) currentState.correctAnswers + 1 else currentState.correctAnswers

            if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                _uiState.value = currentState.copy(
                    currentQuestionIndex = currentState.currentQuestionIndex + 1,
                    selectedAnswer = -1,
                    score = newScore,
                    correctAnswers = newCorrectAnswers
                )
            } else {
                // Quiz finished
                _uiState.value = currentState.copy(
                    isQuizFinished = true,
                    score = newScore,
                    correctAnswers = newCorrectAnswers
                )
            }
        }
    }

    fun restartQuiz(themeId: String) {
        _uiState.value = QuizUiState()
        loadQuiz(themeId)
    }
}

data class QuizUiState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 0,
    val selectedAnswer: Int = -1,
    val score: Int = 0,
    val correctAnswers: Int = 0,
    val isQuizFinished: Boolean = false,
    val error: String? = null
)