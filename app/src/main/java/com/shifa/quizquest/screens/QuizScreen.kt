package com.shifa.quizquest.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.shifa.quizquest.models.Question
import com.shifa.quizquest.models.QuizViewModel
import com.shifa.quizquest.ui.theme.poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    themeId: String,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(themeId) {
        viewModel.loadQuiz(themeId)
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF85E4DC), Color(0xFF3FA1B7))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = if (uiState.totalQuestions > 0)
                        "Soal ${uiState.currentQuestionIndex + 1}/${uiState.totalQuestions}"
                    else "Kuis",
                    fontFamily = poppins,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error!!,
                        color = Color.White,
                        fontFamily = poppins,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigateUp() }
                    ) {
                        Text("Kembali")
                    }
                }
            }

            uiState.isQuizFinished -> {
                QuizResultScreen(
                    score = uiState.score,
                    correctAnswers = uiState.correctAnswers,
                    totalQuestions = uiState.totalQuestions,
                    onRetry = { viewModel.restartQuiz() },
                    onFinish = { navController.navigateUp() }
                )
            }

            else -> {
                QuizQuestionScreen(
                    question = uiState.questions.getOrNull(uiState.currentQuestionIndex),
                    selectedAnswer = uiState.selectedAnswer,
                    onAnswerSelected = { viewModel.selectAnswer(it) },
                    onNextQuestion = { viewModel.nextQuestion() },
                    currentScore = uiState.score
                )
            }
        }
    }
}

@Composable
fun QuizQuestionScreen(
    question: Question?,
    selectedAnswer: Int,
    onAnswerSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    currentScore: Int
) {
    if (question == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Score Display
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Text(
                text = "Skor: $currentScore",
                modifier = Modifier.padding(16.dp),
                fontFamily = poppins,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3FA1B7)
            )
        }

        // Question
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(
                text = question.question,
                modifier = Modifier.padding(20.dp),
                fontFamily = poppins,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        // Answer Options
        question.options.forEachIndexed { index, option ->
            AnswerOption(
                text = option,
                isSelected = selectedAnswer == index,
                onClick = { onAnswerSelected(index) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next Button
        Button(
            onClick = onNextQuestion,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = selectedAnswer != -1,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Lanjut",
                fontFamily = poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AnswerOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF85E4DC) else Color.White
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF3FA1B7)) else null
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            fontFamily = poppins,
            fontSize = 16.sp,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
fun QuizResultScreen(
    score: Int,
    correctAnswers: Int,
    totalQuestions: Int,
    onRetry: () -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Kuis Selesai!",
                    fontFamily = poppins,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3FA1B7)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Skor Anda: $score",
                    fontFamily = poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Benar: $correctAnswers/$totalQuestions",
                    fontFamily = poppins,
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Ulangi")
                    }

                    Button(
                        onClick = onFinish,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Selesai")
                    }
                }
            }
        }
    }
}