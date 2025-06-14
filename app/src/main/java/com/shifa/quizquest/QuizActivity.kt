package com.shifa.quizquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

class QuizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizScreen()
        }
    }
}

@Composable
fun QuizScreen(
    questionNumber: Int = 1,
    totalQuestions: Int = 24,
    question: String = "Hewan-hewan apa yang …",
    answers: List<String> = listOf("Answer 1", "Answer 2", "Answer 3", "Answer 4"),
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    onSubmit: () -> Unit = {},
    navController: NavHostController? = null
) {
    val bgColor = Color(0xFF7ED6D1)
    val selectedAnswer = remember { mutableStateOf(-1) }
    val showDialog = remember { mutableStateOf(false) }
    val showSubmitConfirm = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        QuizHeader(
            modifier = Modifier.align(Alignment.TopStart)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFAEDC81), shape = RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Question $questionNumber",
                        color = Color.Black,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { showDialog.value = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4AA3C3)),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "Navigasi ",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "$questionNumber/$totalQuestions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.Yellow
                    )
                }
            }

            QuizCard(
                question = question,
                answers = answers,
                selectedAnswer = selectedAnswer.value,
                onSelectAnswer = { selectedAnswer.value = it },
                modifier = Modifier.weight(1.0f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onBack,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAEDC81)),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Back")
                }

                Button(
                    onClick = onNext,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAEDC81)),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Next")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { showSubmitConfirm.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Submit",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

        }

        if (showDialog.value) {
            NavigationDialog(
                totalQuestions = totalQuestions,
                currentNumber = questionNumber,
                onDismiss = { showDialog.value = false },
                onSelect = { selected ->
                    println("Navigasi ke soal $selected")
                    showDialog.value = false
                }
            )
        }

        if (showSubmitConfirm.value) {
            AlertDialog(
                onDismissRequest = { showSubmitConfirm.value = false },
                title = { Text("Konfirmasi") },
                text = { Text("Submit?") },
                confirmButton = {
                    Button(onClick = {
                        showSubmitConfirm.value = false
                        onSubmit()
                    }) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showSubmitConfirm.value = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}


@Composable
fun NavigationDialog(
    totalQuestions: Int,
    currentNumber: Int,
    onDismiss: () -> Unit,
    onSelect: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Tutup")
            }
        },
        title = { Text("Navigasi Soal") },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(totalQuestions) { idx ->
                    val isCurrent = (idx + 1) == currentNumber
                    Button(
                        onClick = { onSelect(idx + 1) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCurrent) Color(0xFF4AA3C3) else Color.LightGray
                        ),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("${idx + 1}")
                    }
                }
            }
        }
    )
}

@Composable
fun QuizHeader(modifier: Modifier) {
    val backgroundColor = Color(0xFFD26969)
    val borderColor = Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .height(60.dp)
            .graphicsLayer {
                shape = CutCornerShape(bottomEnd = 32.dp)
                clip = true
            }
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = CutCornerShape(bottomEnd = 32.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "PENGERJAAN KUIS",
            modifier = Modifier.padding(start = 20.dp),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun QuizCard(
    question: String,
    answers: List<String>,
    selectedAnswer: Int,
    onSelectAnswer: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Pertanyaan
            Text(
                text = question,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Jawaban
            answers.forEachIndexed { index, answer ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clickable { onSelectAnswer(index) }
                ) {
                    RadioButton(
                        selected = selectedAnswer == index,
                        onClick = { onSelectAnswer(index) }
                    )
                    Text(
                        text = answer,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizScreen()
}
