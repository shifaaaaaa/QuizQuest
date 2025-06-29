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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shifa.quizquest.ui.theme.poppins
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayScreen(
    navController: NavController,
    quizId: Int = 1
) {
    val questions = remember { QuizRepository.getQuestionsByQuizId(quizId) }
    // Memastikan quizInfo adalah String
    val quizInfo: String = remember(quizId) {
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

    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(30) }

    val coroutineScope = rememberCoroutineScope()
    val quizStartTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var finalResult by remember { mutableStateOf<QuizResult?>(null) }

    LaunchedEffect(key1 = currentIndex) {
        timeLeft = 30
        selectedAnswer = null
        if (!showResult) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
        }
    }

    if (showResult && finalResult != null) {
        QuizResultDisplay(result = finalResult!!, navController = navController) {
            // Logic untuk Main Lagi
            showResult = false
            finalResult = null
            currentIndex = 0
            score = 0
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        // Pemanggilan Text yang sudah benar
                        Text(
                            text = quizInfo,
                            fontFamily = poppins,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        Card(colors = CardDefaults.cardColors(containerColor = if (timeLeft > 10) Color(0xFF4CAF50) else Color(0xFFF44336))) {
                            Text("⏳ $timeLeft", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        ) { paddingValues ->
            val question = questions[currentIndex]
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)) {
                // UI Pengerjaan Kuis (Progress, Pertanyaan, Pilihan Jawaban)
                // ... (Kode untuk progress, pertanyaan, dan pilihan jawaban tidak berubah) ...
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
                LinearProgressIndicator(
                    progress = (currentIndex + 1).toFloat() / questions.size,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF3FA1B7)
                )
                Spacer(modifier = Modifier.height(32.dp))
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
                question.choices.forEachIndexed { index, choice ->
                    val isSelected = choice == selectedAnswer
                    val optionLabel = listOf("A", "B", "C", "D")[index]
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        onClick = { selectedAnswer = choice },
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF3FA1B7) else Color.White),
                        border = CardDefaults.outlinedCardBorder().copy(width = if (isSelected) 2.dp else 1.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = if (isSelected) Color.White else Color(0xFF3FA1B7)),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = optionLabel, color = if (isSelected) Color(0xFF3FA1B7) else Color.White, fontWeight = FontWeight.Bold, fontFamily = poppins)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = choice, color = if (isSelected) Color.White else Color.Black, fontSize = 16.sp, fontFamily = poppins, modifier = Modifier.weight(1f))
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (selectedAnswer == questions[currentIndex].correctAnswer) {
                            score++
                        }
                        if (currentIndex < questions.lastIndex) {
                            currentIndex++
                        } else {
                            // Logic saat kuis selesai
                            val quizEndTime = System.currentTimeMillis()
                            val timeTakenSeconds = TimeUnit.MILLISECONDS.toSeconds(quizEndTime - quizStartTime).toInt()
                            val user = Firebase.auth.currentUser
                            if (user != null) {
                                val resultData = QuizResultData(
                                    userId = user.uid,
                                    quizId = quizId,
                                    quizTitle = quizInfo,
                                    score = score,
                                    totalQuestions = questions.size,
                                    timeTakenSeconds = timeTakenSeconds
                                )
                                coroutineScope.launch { QuizRepository.saveQuizResult(resultData) }
                                finalResult = resultData.toUiModel("local")
                            }
                            showResult = true
                        }
                    },
                    enabled = selectedAnswer != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (currentIndex == questions.lastIndex) "Selesai" else "Lanjut", fontSize = 16.sp, fontFamily = poppins)
                }
            }
        }
    }
}

// Composable untuk menampilkan hasil, pastikan sudah ada
@Composable
fun QuizResultDisplay(result: QuizResult, navController: NavController, onPlayAgain: () -> Unit) {
    // ... Implementasi dari respons sebelumnya, tidak berubah ...
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🎉 Kuis Selesai! 🎉", fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = poppins)
        Spacer(modifier = Modifier.height(16.dp))
        Text(result.quizTitle, fontSize = 20.sp, fontFamily = poppins, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = result.performanceColor.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Skor Akhir", fontSize = 16.sp, fontFamily = poppins, color = Color.Gray)
                Text("${result.score} / ${result.totalQuestions}", fontSize = 36.sp, fontWeight = FontWeight.Bold, fontFamily = poppins, color = result.performanceColor)
                Text("${result.percentage}%", fontSize = 18.sp, fontFamily = poppins, color = result.performanceColor)
                Spacer(modifier = Modifier.height(16.dp))
                result.performanceText?.let {
                    Text(it, fontSize = 16.sp, fontFamily = poppins, fontWeight = FontWeight.Medium)
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f)) {
                Text("Kembali", fontFamily = poppins)
            }
            Button(onClick = onPlayAgain, modifier = Modifier.weight(1f)) {
                Text("Main Lagi", fontFamily = poppins)
            }
        }
    }
}