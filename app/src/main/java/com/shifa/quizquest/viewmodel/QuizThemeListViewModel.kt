package com.shifa.quizquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shifa.quizquest.models.QuizTheme
import com.shifa.quizquest.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizThemeListViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizThemeListUiState())
    val uiState: StateFlow<QuizThemeListUiState> = _uiState.asStateFlow()

    init {
        loadQuizThemes()
    }

    private fun loadQuizThemes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            quizRepository.getQuizThemes()
                .onSuccess { themes ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        quizThemes = themes,
                        error = if (themes.isEmpty()) "Tidak ada tema kuis tersedia" else null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Gagal memuat tema kuis: ${exception.message}"
                    )
                }
        }
    }

    fun retryLoading() {
        loadQuizThemes()
    }
}

data class QuizThemeListUiState(
    val isLoading: Boolean = false,
    val quizThemes: List<QuizTheme> = emptyList(),
    val error: String? = null
)