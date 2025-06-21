package com.shifa.quizquest.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.shifa.quizquest.R
import com.shifa.quizquest.Screen
import com.shifa.quizquest.models.QuizTheme
import com.shifa.quizquest.ui.theme.poppins
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import com.shifa.quizquest.viewmodel.QuizThemeListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizThemeListScreen(
    navController: NavController,
    viewModel: QuizThemeListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                    text = "Pilih Tema Kuis",
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

        // Content
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
                        text = uiState.error,
                        color = Color.White,
                        fontFamily = poppins,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.retryLoading() }
                    ) {
                        Text("Coba Lagi")
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.quizThemes) { theme ->
                        QuizThemeCard(
                            theme = theme,
                            onClick = {
                                navController.navigate(Screen.Quiz.createRoute(theme.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizThemeCard(
    theme: QuizTheme,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Theme Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.1f))
            ) {
                if (theme.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = theme.imageUrl,
                        contentDescription = theme.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_quiz_placeholder),
                        contentDescription = theme.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Inside
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Theme Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = theme.name,
                    fontFamily = poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = theme.description,
                    fontFamily = poppins,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Surface(
                        onClick = { },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFF85E4DC).copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            text = "${theme.questionCount} soal",
                            fontSize = 12.sp,
                            color = Color(0xFF3FA1B7)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        onClick = { },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFC5E1A5).copy(alpha = 0.3f)
                        )) {
                        Text(
                            text = theme.difficulty,
                            fontSize = 12.sp,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }
        }
    }
}