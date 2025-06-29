package com.shifa.quizquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shifa.quizquest.ui.theme.poppins
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            DashboardScreen(navController = navController)
        }
    }
}

@Composable
fun DashboardScreen(navController: NavController, viewModel: DashboardViewModel = viewModel()) {
    val profile by viewModel.profileData.collectAsState()

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF85E4DC), Color(0xFF3FA1B7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.add(WindowInsets.navigationBars).asPaddingValues())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HeaderSection(
                    userName = profile.nickname.ifBlank { "Pengguna" },
                    totalScore = 1234,
                    imageResId = profile.imageRes,
                    navController = navController
                )
            }
            item { SummaryCards() }
            item { ActionButtons(navController = navController) }
            item { LeaderboardButton() }
            item { RecentQuizzes() }
        }
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
fun SummaryCards() {
    val cardData = listOf(
        Triple("Kuis Diikuti", "0", Color(0xFFB2EBF2)),
        Triple("Skor Rata-rata", "0", Color(0xFFC5E1A5)),
        Triple("Akurasi", "0%", Color(0xFFFFF59D))
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
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

@Composable
fun RecentQuizzes() {
    Column {
        Text(
            text = "Kuis Terbaru",
            fontFamily = poppins,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        val quizzes = listOf(
            "Quiz Musik", "Quiz Film", "Sejarah", "Bahasa Inggris",
            "Matematika Dasar", "Trivia", "Lawak", "Tebak Gambar", "Kebudayaan Indonesia"
        )
        quizzes.forEach { quiz ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = quiz,
                        fontFamily = poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Mulai",
                        fontFamily = poppins,
                        fontSize = 14.sp,
                        color = Color.Blue
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    DashboardScreen(navController = rememberNavController())
}