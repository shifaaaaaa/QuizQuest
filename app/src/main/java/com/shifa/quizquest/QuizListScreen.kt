package com.shifa.quizquest

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.shifa.quizquest.ui.theme.poppins
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

data class Quiz(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: String,
    val questions: Int,
    val participants: Int,
    val rating: Float,
    val backgroundColor: Color,
    val icon: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizListScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("Semua") }

    val quizzes = listOf(
        Quiz(1, "Quiz Musik", "Tes pengetahuan musik dari berbagai genre dan era", "Mudah", 15, 1234, 4.5f, Color(0xFFE1BEE7), "🎵"),
        Quiz(2, "Quiz Film", "Tantangan tentang film Hollywood dan Indonesia", "Sedang", 20, 987, 4.3f, Color(0xFFFFCDD2), "🎬"),
        Quiz(3, "Sejarah", "Jelajahi peristiwa bersejarah dunia dan Indonesia", "Sulit", 25, 756, 4.7f, Color(0xFFFFF9C4), "📚"),
        Quiz(4, "Bahasa Inggris", "Uji kemampuan vocabulary dan grammar", "Sedang", 18, 1456, 4.4f, Color(0xFFBBDEFB), "🇬🇧"),
        Quiz(5, "Matematika Dasar", "Soal matematika untuk tingkat pemula", "Mudah", 12, 2134, 4.2f, Color(0xFFC8E6C9), "🔢"),
        Quiz(6, "Trivia Umum", "Pengetahuan umum dari berbagai topik menarik", "Sedang", 22, 1876, 4.6f, Color(0xFFF8BBD9), "🧠"),
        Quiz(7, "Lawak & Humor", "Quiz tentang komedi dan humor Indonesia", "Mudah", 16, 543, 4.1f, Color(0xFFFFF59D), "😂"),
        Quiz(8, "Tebak Gambar", "Tebak objek, tempat, dan tokoh dari gambar", "Sedang", 20, 1123, 4.5f, Color(0xFFC5CAE9), "🖼️"),
        Quiz(9, "Kebudayaan Indonesia", "Kekayaan budaya Nusantara dari Sabang sampai Merauke", "Sulit", 30, 678, 4.8f, Color(0xFFFFCC80), "🇮🇩")
    )

    val categories = listOf("Semua", "Mudah", "Sedang", "Sulit")

    val filteredQuizzes = if (selectedCategory == "Semua") {
        quizzes
    } else {
        quizzes.filter { it.difficulty == selectedCategory }
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF85E4DC), Color(0xFF3FA1B7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.add(WindowInsets.navigationBars).asPaddingValues())
        ) {
            // Header
            TopAppBar(
                title = {
                    Text(
                        text = "Pilih Kuis",
                        fontFamily = poppins,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("dashboard_screen") {
                            launchSingleTop = true
                            popUpTo("dashboard_screen") { inclusive = false }
                        }
                    })
                    {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // Category Filter
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        selected = selectedCategory == category,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.White,
                            selectedLabelColor = Color(0xFF3FA1B7),
                            containerColor = Color.White.copy(alpha = 0.2f),
                            labelColor = Color.White
                        )
                    )
                }
            }

            // Quiz List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredQuizzes) { quiz ->
                    QuizCard(
                        quiz = quiz,
                        onStartQuiz = {
                            // Navigate ke quiz detail
                            navController.navigate("quiz_play_Screen/${quiz.id}")
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun QuizCard(quiz: Quiz, onStartQuiz: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStartQuiz() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = quiz.backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with icon and difficulty
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = quiz.icon,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = quiz.title,
                        fontFamily = poppins,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                AssistChip(
                    onClick = { },
                    label = { Text(quiz.difficulty, fontSize = 12.sp) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (quiz.difficulty) {
                            "Mudah" -> Color(0xFF4CAF50)
                            "Sedang" -> Color(0xFFFF9800)
                            "Sulit" -> Color(0xFFF44336)
                            else -> Color.Gray
                        },
                        labelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = quiz.description,
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = "Durasi Kuis",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${quiz.questions} soal",
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.6f)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${quiz.participants}",
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.6f)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFFFFD700)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = quiz.rating.toString(),
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.6f)
                        )
                    }
                }

                Button(
                    onClick = onStartQuiz,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3FA1B7)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mulai", fontSize = 14.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewQuizListScreen() {
    QuizListScreen(navController = rememberNavController())
}

