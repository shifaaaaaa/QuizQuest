package com.shifa.quizquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shifa.quizquest.ui.theme.poppins

@Composable
fun DashboardScreen(navController: NavController, viewModel: DashboardViewModel = viewModel()) {
    val profile by viewModel.profileData.collectAsState()
    val recentResults by viewModel.recentQuizResults.collectAsState()
    val stats by viewModel.dashboardStats.collectAsState()

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF85E4DC), Color(0xFF3FA1B7))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .verticalScroll(rememberScrollState())
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        HeaderSection(
            userName = profile.nickname.ifBlank { "Pengguna" },
            totalScore = stats.accuracy,
            imageResId = profile.imageRes,
            navController = navController
        )
        Spacer(modifier = Modifier.height(16.dp))
        SummaryCards(
            quizzesTaken = stats.quizzesTaken,
            averageScore = stats.averageScore,
            accuracy = stats.accuracy
        )
        Spacer(modifier = Modifier.height(16.dp))
        ActionButtons(navController = navController)
        Spacer(modifier = Modifier.height(16.dp))
        LeaderboardButton()
        Spacer(modifier = Modifier.height(16.dp))
        RecentQuizzesSection(results = recentResults)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun HeaderSection(
    userName: String,
    totalScore: Int,
    imageResId: Int,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(end = 12.dp)
        ) {
            Text(
                text = "Selamat datang, $userName!",
                fontFamily = poppins,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Skor Total: $totalScore",
                fontFamily = poppins,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Box {
            Box(
                modifier = Modifier.run {
                    size(48.dp)
                        .clickable(
                            indication = androidx.compose.material3.ripple(bounded = true),
                            interactionSource = remember { MutableInteractionSource() }
                        ) { expanded = true }
                        .border(1.dp, Color.Gray, CircleShape)
                        .shadow(2.dp, CircleShape)
                }
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Profile") },
                    onClick = {
                        expanded = false
                        navController.navigate(Screen.Profile.route)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = {
                        expanded = false
                        navController.navigate(Screen.Settings.route)
                    }
                )
            }
        }
    }
}

@Composable
fun SummaryCards(quizzesTaken: Int, averageScore: Int, accuracy: Int) {
    val cardData = listOf(
        Triple("Kuis Diikuti", quizzesTaken.toString(), Color(0xFFB2EBF2)),
        Triple("Skor Rata-rata", "$averageScore%", Color(0xFFC5E1A5)),
        Triple("Akurasi", "$accuracy%", Color(0xFFFFF59D))
    )
    Row(
        modifier = Modifier.fillMaxWidth().height(110.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        cardData.forEach { (title, value, color) ->
            SummaryCard(title = title, value = value, backgroundColor = color)
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ActionButtons(navController: NavController) {
    Column {
        Button(
            onClick = {
                navController.navigate(Screen.QuizList.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Mulai Kuis Baru",
                fontFamily = poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LeaderboardButton() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("Lihat Leaderboard", fontSize = 16.sp)
    }
}


// --- FUNGSI INI SEKARANG ADA DI SINI ---
@Composable
fun RecentQuizzesSection(results: List<QuizResultData>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Kuis Terbaru",
            fontFamily = poppins,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (results.isEmpty()) {
            Text(
                "Kamu belum mengerjakan kuis.",
                fontFamily = poppins,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                results.forEach { resultData ->
                    RecentQuizCard(resultData = resultData)
                }
            }
        }
    }
}

// --- FUNGSI INI JUGA SEKARANG ADA DI SINI ---
@Composable
fun RecentQuizCard(resultData: QuizResultData) {
    val result = resultData.toUiModel(id = "temp_id")
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = result.quizTitle, fontFamily = poppins, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Pada: ${result.completedAtFormatted}", fontFamily = poppins, fontSize = 12.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${result.score}/${result.totalQuestions}", fontFamily = poppins, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = result.performanceColor)
                Text(text = "${result.percentage}%", fontFamily = poppins, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}