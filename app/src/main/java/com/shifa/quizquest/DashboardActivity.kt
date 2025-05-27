package com.shifa.quizquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shifa.quizquest.ui.theme.poppins

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardScreen()
        }
    }
}

@Composable
fun DashboardScreen() {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF85E4DC), Color(0xFF3FA1B7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .verticalScroll(rememberScrollState())
            .padding(
                top = 48.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        Column {
            HeaderSection(userName = "UserXXX", totalScore = 1234)
            Spacer(modifier = Modifier.height(16.dp))
            SummaryCards()
            Spacer(modifier = Modifier.height(16.dp))
            ActionButtons()
            Spacer(modifier = Modifier.height(16.dp))
            LeaderboardButton()
            Spacer(modifier = Modifier.height(16.dp))
            RecentQuizzes()
        }
    }
}

@Composable
fun HeaderSection(userName: String, totalScore: Int) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
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
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, shape = CircleShape)
                    .clickable { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Profile") },
                    onClick = {
                        expanded = false
                        // TODO: Navigate to ProfileActivity
                    }
                )
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = {
                        expanded = false
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
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
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
fun ActionButtons() {
    Column {
        Button(
            onClick = { /* TODO: Navigate to quiz */ },
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
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { /* TODO: Navigate to quiz history */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.White)
        ) {
            Text(
                text = "Lihat Riwayat",
                fontFamily = poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun LeaderboardButton() {
    Button(
        onClick = {
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
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
        val quizzes = listOf("Matematika Dasar", "Sejarah Indonesia", "Bahasa Inggris")
        quizzes.forEach { quiz ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
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
    DashboardScreen()
}