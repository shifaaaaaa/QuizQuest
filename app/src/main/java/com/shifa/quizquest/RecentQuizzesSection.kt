package com.shifa.quizquest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shifa.quizquest.ui.theme.poppins

@Composable
fun RecentQuizzesSection(
    results: List<QuizResultData>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
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
                text = "Kamu belum mengerjakan kuis.",
                fontFamily = poppins,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
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

@Composable
fun RecentQuizCard(resultData: QuizResultData) {
    val result = resultData.toUiModel(id = "temp_id")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.quizTitle,
                    fontFamily = poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pada: ${result.completedAtFormatted}",
                    fontFamily = poppins,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${result.score}/${result.totalQuestions}",
                    fontFamily = poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = result.performanceColor
                )
                Text(
                    text = "${result.percentage}%",
                    fontFamily = poppins,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}