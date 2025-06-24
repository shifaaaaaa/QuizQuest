package com.shifa.quizquest

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shifa.quizquest.ui.theme.poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayScreen(
    navController: NavController,
    quizId: Int = 1 // Parameter untuk menerima quiz ID
) {
    // Ambil soal berdasarkan quiz ID
    val questions = remember { QuizRepository.getQuestionsByQuizId(quizId) }

    // Ambil informasi quiz untuk menampilkan judul
    val quizInfo = remember {
        when (quizId) {
            1 -> "Quiz Musik"
            2 -> "Quiz Film"
            3 -> "Sejarah"
            4 -> "Bahasa Inggris"
            5 -> "Matematika Dasar"
            6 -> "Trivia Umum"
            7 -> "Lawak & Humor"
            8 -> "Tebak Gambar"
            9 -> "Kebudayaan Indonesia"
            else -> "Quiz Umum"
        }
    }

    // State untuk soal saat ini, jawaban, dan skor
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(30) } // Timer 30 detik per soal

    // Timer effect
    LaunchedEffect(currentIndex) {
        timeLeft = 30
        selectedAnswer = null
    }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0 && !showResult && selectedAnswer == null) {
            kotlinx.coroutines.delay(1000L)
            timeLeft--
        } else if (timeLeft == 0 && selectedAnswer == null) {
            // Auto next jika waktu habis
            if (currentIndex < questions.lastIndex) {
                currentIndex++
            } else {
                showResult = true
            }
        }
    }

    if (showResult) {
        // Hasil kuis
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🎉 Kuis Selesai! 🎉",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                quizInfo,
                fontSize = 20.sp,
                fontFamily = poppins,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3FA1B7).copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Skor Akhir",
                        fontSize = 16.sp,
                        fontFamily = poppins,
                        color = Color.Gray
                    )
                    Text(
                        "$score / ${questions.size}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppins,
                        color = Color(0xFF3FA1B7)
                    )

                    val percentage = (score.toFloat() / questions.size * 100).toInt()
                    Text(
                        "$percentage%",
                        fontSize = 18.sp,
                        fontFamily = poppins,
                        color = when {
                            percentage >= 80 -> Color(0xFF4CAF50)
                            percentage >= 60 -> Color(0xFFFF9800)
                            else -> Color(0xFFF44336)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        when {
                            percentage >= 80 -> "Excellent! 🌟"
                            percentage >= 60 -> "Good Job! 👍"
                            else -> "Keep Learning! 📚"
                        },
                        fontSize = 16.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Kembali", fontFamily = poppins)
                }

                Button(
                    onClick = {
                        // Reset untuk main lagi
                        currentIndex = 0
                        selectedAnswer = null
                        score = 0
                        showResult = false
                        timeLeft = 30
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3FA1B7)
                    )
                ) {
                    Text("Main Lagi", fontFamily = poppins)
                }
            }
        }
    } else {
        val question = questions[currentIndex]

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header dengan back button dan timer
            TopAppBar(
                title = {
                    Text(
                        text = quizInfo,
                        fontFamily = poppins,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                timeLeft > 20 -> Color(0xFF4CAF50)
                                timeLeft > 10 -> Color(0xFFFF9800)
                                else -> Color(0xFFF44336)
                            }
                        )
                    ) {
                        Text(
                            text = "⏱️ $timeLeft",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Progress indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Soal ${currentIndex + 1} dari ${questions.size}",
                        fontSize = 14.sp,
                        fontFamily = poppins,
                        color = Color.Gray
                    )
                    Text(
                        "Skor: $score",
                        fontSize = 14.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3FA1B7)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress bar
                LinearProgressIndicator(
                    progress = (currentIndex + 1).toFloat() / questions.size,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF3FA1B7)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Pertanyaan
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF3FA1B7).copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = question.text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppins,
                        modifier = Modifier.padding(20.dp),
                        lineHeight = 28.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Pilihan jawaban
                question.choices.forEachIndexed { index, choice ->
                    val isSelected = choice == selectedAnswer
                    val optionLabel = listOf("A", "B", "C", "D")[index]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        onClick = { selectedAnswer = choice },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF3FA1B7) else Color.White
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            width = if (isSelected) 2.dp else 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color.White else Color(0xFF3FA1B7)
                                ),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = optionLabel,
                                        color = if (isSelected) Color(0xFF3FA1B7) else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = poppins
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = choice,
                                color = if (isSelected) Color.White else Color.Black,
                                fontSize = 16.sp,
                                fontFamily = poppins,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Tombol Lanjut
                Button(
                    onClick = {
                        if (selectedAnswer == question.correctAnswer) {
                            score++
                        }
                        if (currentIndex < questions.lastIndex) {
                            currentIndex++
                        } else {
                            showResult = true
                        }
                    },
                    enabled = selectedAnswer != null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3FA1B7)
                    )
                ) {
                    Text(
                        text = if (currentIndex == questions.lastIndex) "Selesai" else "Lanjut",
                        fontSize = 16.sp,
                        fontFamily = poppins,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewQuizPlayScreen() {
    QuizPlayScreen(navController = rememberNavController())
}